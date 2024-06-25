package pe.edu.upc.TFFinanzas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upc.TFFinanzas.dtos.CreditoTDO;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;
import pe.edu.upc.TFFinanzas.entities.Credito;
import pe.edu.upc.TFFinanzas.entities.DetalleCredito;
import pe.edu.upc.TFFinanzas.entities.PlazoGraciaEnum;
import pe.edu.upc.TFFinanzas.entities.Cliente;
import pe.edu.upc.TFFinanzas.entities.NumeroDiasCuotaEnum;
import pe.edu.upc.TFFinanzas.entities.TipoCreditoEnum;
import pe.edu.upc.TFFinanzas.entities.TipoInteresEnum;
import pe.edu.upc.TFFinanzas.repositories.CreditoRepository;
import pe.edu.upc.TFFinanzas.repositories.DetalleCreditoRepository;
import pe.edu.upc.TFFinanzas.repositories.ClienteRespositoy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditoService {
    private final CreditoRepository creditoRepository;
    private final ClienteRespositoy clienteRepository;
    private final DetalleVentaService detalleVentaService;
    private final DetalleCreditoRepository detalleCreditoRepository;

    private static final double ANNUAL_INTEREST_RATE = 0.10;
    private static final int DAYS_IN_YEAR = 360;


//! REGISTRAR CREDITO
public ResponseDTO registrarCredito(CreditoTDO creditoDTO) {
    Optional<Cliente> clienteOpt = clienteRepository.findById(creditoDTO.getIdCliente());
    if (!clienteOpt.isPresent()) {
        return new ResponseDTO("Cliente no encontrado");
    }
    if (creditoDTO.getTipoCredito().equals(TipoCreditoEnum.CORTO_PLAZO)) {
        if (!creditoDTO.getTipoInteres().equals(TipoInteresEnum.NOMINAL) && !creditoDTO.getTipoInteres().equals(TipoInteresEnum.EFECTIVO)) {
            return new ResponseDTO("Para créditos a corto plazo, el tipo de interés solo puede ser NOMINAL o EFECTIVO.");
        }
        if (creditoDTO.getNumeroDiasCuota() != NumeroDiasCuotaEnum.CERO || creditoDTO.getPlazoGracia() != PlazoGraciaEnum.CERO) {
            return new ResponseDTO("Para créditos a corto plazo, los días de cuota y el plazo de gracia deben ser CERO.");
        }
    } else if (creditoDTO.getTipoCredito().equals(TipoCreditoEnum.LARGO_PLAZO)) {
        if (!creditoDTO.getTipoInteres().equals(TipoInteresEnum.ANUALIDAD_SIMPLE)) {
            return new ResponseDTO("Para créditos a largo plazo, el tipo de interés solo puede ser ANUALIDAD_SIMPLE.");
        }
        if (creditoDTO.getPlazoGracia() == PlazoGraciaEnum.CERO && creditoDTO.getNumeroDiasCuota() == NumeroDiasCuotaEnum.CERO) {
            return new ResponseDTO("Si el plazo de gracia es CERO, los días de cuota no deben de ser CERO.");
        }
        if (creditoDTO.getPlazoGracia() != PlazoGraciaEnum.CERO && creditoDTO.getNumeroDiasCuota() == NumeroDiasCuotaEnum.CERO) {
            return new ResponseDTO("Si el plazo de gracia es mayor a CERO, los días de cuota no pueden ser CERO.");
        }
    }

    double montoInicial = detalleVentaService.calcularMontoTotal(creditoDTO.getIdCliente());
    double tiempoEnDias = ChronoUnit.DAYS.between(creditoDTO.getFechaInicio(), creditoDTO.getFechaFin());
    double tiempoEnAnios = tiempoEnDias / DAYS_IN_YEAR;
    double valorFuturo = 0.0;
    double renta = 0.0;
    double tep = calculoTEP(creditoDTO.getNumeroDiasCuota().getDias());

    if (creditoDTO.getTipoCredito().equals(TipoCreditoEnum.CORTO_PLAZO)) {
        if (creditoDTO.getTipoInteres().equals(TipoInteresEnum.NOMINAL)) {
            valorFuturo = calcularValorFuturoNominal(montoInicial, tiempoEnAnios);
        } else if (creditoDTO.getTipoInteres().equals(TipoInteresEnum.EFECTIVO)) {
            valorFuturo = calcularValorFuturoEfectivo(montoInicial, tiempoEnAnios);
        }
    } else if (creditoDTO.getTipoCredito().equals(TipoCreditoEnum.LARGO_PLAZO)) {
        int numeroDiasCuota = creditoDTO.getNumeroDiasCuota().getDias();
        int periodosDeGracia = creditoDTO.getPlazoGracia().getCuotas();
        double n = tiempoEnDias / numeroDiasCuota;
        //double tep = calculoTEP(numeroDiasCuota);

        if (creditoDTO.getPlazoGracia() == PlazoGraciaEnum.CERO) {
            renta = calcularRentaFrancesa(montoInicial, tep, (int) n);
            valorFuturo = valorFuturoSinGracia(renta, tep, n);
        } else {
            //double montoAcumuladoConGracia = montoAcumuladoConGracia(montoInicial, tep, periodosDeGracia);
            //renta = rentaConGracia(montoAcumuladoConGracia, tep, n - periodosDeGracia);
            // valorFuturo = valorFuturoConGracia(renta, tep, n - periodosDeGracia);
            valorFuturo = valorFuturoConGracia(calcularRentaFrancesa(montoInicial, tep, (int) n - periodosDeGracia), tep, n - periodosDeGracia);
        }
    }

        Credito credito = Credito.builder()
            .monto((float) valorFuturo)
            .fechaInicio(creditoDTO.getFechaInicio())
            .fechaFin(creditoDTO.getFechaFin())
            .estado(true)
            .TipoCredito(creditoDTO.getTipoCredito())
            .tipoInteres(creditoDTO.getTipoInteres())
            .plazoGracia(creditoDTO.getPlazoGracia())
            .numeroDiasCuota(creditoDTO.getNumeroDiasCuota())
            .cliente(clienteOpt.get())
            .build();

    credito = creditoRepository.save(credito);
    generarCronogramaPagos(credito, creditoDTO, tep);
    return new ResponseDTO("Crédito registrado correctamente");
}

private void generarCronogramaPagos(Credito credito, CreditoTDO creditoDTO, double tep) {
    LocalDate fechaInicio = credito.getFechaInicio();
    int numeroDiasCuota = creditoDTO.getNumeroDiasCuota().getDias();
    int periodosDeGracia = creditoDTO.getPlazoGracia().getCuotas();
    double montoInicial = detalleVentaService.calcularMontoTotal(creditoDTO.getIdCliente());
    double saldoInicial = montoInicial;
    int numeroPeriodos;
    

    if (creditoDTO.getTipoCredito().equals(TipoCreditoEnum.CORTO_PLAZO)) {
        LocalDate fechaFin = credito.getFechaFin();
        double tiempoEnDias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);

        if(creditoDTO.getTipoInteres().equals(TipoInteresEnum.NOMINAL)){
        // Para créditos a corto plazo
        double valorFuturo = calcularValorFuturoNominal(montoInicial, tiempoEnDias / DAYS_IN_YEAR);
        double interes = valorFuturo - montoInicial;

        DetalleCredito detalle = new DetalleCredito();
        detalle.setSaldoInicial((float) montoInicial);
        detalle.setFechaPagoCuota(fechaFin);
        detalle.setRenta((float) valorFuturo);
        detalle.setInteres((float) interes);
        detalle.setAmortizacion((float) montoInicial);
        detalle.setSaldoFinal(0.0f);
        detalle.setEstadoPago(false);
        //detalle.setMora(0.0f);
        detalle.setCredito(credito);
        detalleCreditoRepository.save(detalle);
        }
        else {
            double valorFuturo = calcularValorFuturoEfectivo(montoInicial, tiempoEnDias / DAYS_IN_YEAR);
            double interes = valorFuturo - montoInicial;
            
            DetalleCredito detalle = new DetalleCredito();
            detalle.setSaldoInicial((float) montoInicial);
            detalle.setFechaPagoCuota(fechaFin);
            detalle.setRenta((float) valorFuturo);
            detalle.setInteres((float) interes);
            detalle.setAmortizacion((float) montoInicial);
            detalle.setSaldoFinal(0.0f);
            detalle.setEstadoPago(false);
            //detalle.setMora(0.0f);
            detalle.setCredito(credito);
            detalleCreditoRepository.save(detalle);
        }

    } else {
        // Para créditos a largo plazo
        if (numeroDiasCuota == 0) {
            throw new IllegalArgumentException("El número de días de cuota no puede ser cero para créditos a largo plazo.");
        }
        numeroPeriodos = (int) Math.ceil(ChronoUnit.DAYS.between(fechaInicio, credito.getFechaFin()) / (double) numeroDiasCuota);
        double renta = calcularRentaFrancesa(montoInicial, tep, numeroPeriodos - periodosDeGracia);

        // Generar pagos durante el periodo de gracia (sin amortización, intereses se capitalizan)
        for (int i = 0; i < periodosDeGracia; i++) {
            DetalleCredito detalle = new DetalleCredito();
            detalle.setSaldoInicial((float) saldoInicial);
            detalle.setFechaPagoCuota(fechaInicio.plusDays(numeroDiasCuota * (i + 1)));
            detalle.setRenta(0.0f);
            detalle.setInteres((float) (saldoInicial * tep));
            detalle.setAmortizacion(0.0f);
            detalle.setSaldoFinal((float) (saldoInicial + detalle.getInteres()));
            detalle.setEstadoPago(false);
            //detalle.setMora(0.0f);
            detalle.setCredito(credito);
            detalleCreditoRepository.save(detalle);
            saldoInicial = detalle.getSaldoFinal();  // Capitalizamos los intereses
        }

        // Generar pagos después del periodo de gracia (con amortización)
        for (int i = periodosDeGracia; i < numeroPeriodos; i++) {
            DetalleCredito detalle = new DetalleCredito();
            detalle.setSaldoInicial((float) saldoInicial);
            detalle.setFechaPagoCuota(fechaInicio.plusDays(numeroDiasCuota * (i + 1)));
            detalle.setInteres((float) (saldoInicial * tep));
            detalle.setRenta((float) renta);
            detalle.setAmortizacion((float) (renta - detalle.getInteres()));
            detalle.setSaldoFinal((float) (saldoInicial - detalle.getAmortizacion()));
            if (i == numeroPeriodos - 1) {
                // Ajustar el saldo final en el último periodo para asegurar que sea 0
                detalle.setSaldoFinal(0.0f);
            }
            detalle.setEstadoPago(false);
            //detalle.setMora(0.0f);
            detalle.setCredito(credito);
            detalleCreditoRepository.save(detalle);
            saldoInicial = detalle.getSaldoFinal();
        }
    }
}

    // //! ACTUALIZAR CREDITO
    // public ResponseDTO actualizarCredito(Long idCredito, CreditoTDO creditoDTO) {
    //     Optional<Credito> creditoOpt = creditoRepository.findById(idCredito);
    //     if (creditoOpt.isPresent()) {
    //         double montoInicial = detalleVentaService.calcularMontoTotal(creditoDTO.getIdCliente());
    //         double tiempoEnDias = ChronoUnit.DAYS.between(creditoDTO.getFechaInicio(), creditoDTO.getFechaFin());
    //         double tiempoEnAnios = tiempoEnDias / DAYS_IN_YEAR;

    //         double valorFuturo = 0.0;

    //         if (creditoDTO.getTipoCredito().equals(TipoCreditoEnum.CORTO_PLAZO)) {
    //             if (creditoDTO.getTipoInteres().equals(TipoInteresEnum.NOMINAL)) {
    //                 valorFuturo = calcularValorFuturoNominal(montoInicial, tiempoEnAnios);
    //             } else if (creditoDTO.getTipoInteres().equals(TipoInteresEnum.EFECTIVO)) {
    //                 valorFuturo = calcularValorFuturoEfectivo(montoInicial, tiempoEnAnios);
    //             }
    //         } else if (creditoDTO.getTipoCredito().equals(TipoCreditoEnum.LARGO_PLAZO)) {
    //             int numeroDiasCuota = creditoDTO.getNumeroDiasCuota().getDias();
    //             int periodosDeGracia = creditoDTO.getPlazoGracia().getCuotas();
    //             double n = tiempoEnDias / numeroDiasCuota;
    //             double tep = calcularTEP(numeroDiasCuota);

    //             if (creditoDTO.getPlazoGracia() == PlazoGraciaEnum.CERO) {
    //                 double renta = rentaSinGracia(montoInicial, tep, n);
    //                 valorFuturo = valorFuturoSinGracia(renta, tep, n);
    //             } else {
    //                 double montoAcumuladoConGracia = montoAcumuladoConGracia(montoInicial, tep, periodosDeGracia);
    //                 double rentaConGracia = rentaConGracia(montoAcumuladoConGracia, tep, n - periodosDeGracia);
    //                 valorFuturo = valorFuturoConGracia(rentaConGracia, tep, n - periodosDeGracia);
    //             }
    //         }

    //         Credito credito = Credito.builder()
    //                 .idCredito(idCredito)
    //                 .monto((float) valorFuturo)
    //                 .fechaInicio(creditoDTO.getFechaInicio())
    //                 .fechaFin(creditoDTO.getFechaFin())
    //                 .estado(creditoDTO.getEstado())
    //                 .TipoCredito(creditoDTO.getTipoCredito())
    //                 .tipoInteres(creditoDTO.getTipoInteres())
    //                 .plazoGracia(creditoDTO.getPlazoGracia())
    //                 .numeroDiasCuota(creditoDTO.getNumeroDiasCuota())
    //                 .cliente(clienteRepository.findById(creditoDTO.getIdCliente()).get())
    //                 .build();

    //         creditoRepository.save(credito);
    //         return new ResponseDTO("Crédito actualizado correctamente");
    //     }
    //     return new ResponseDTO("Crédito no encontrado");
    // }

    //! LISTAR TODOS LOS CREDITOS
    public List<CreditoTDO> listarCreditos() {
        List<Credito> creditos = creditoRepository.findAll();
        return creditos.stream()
                .map(credito -> new CreditoTDO(
                        credito.getIdCredito(),
                        credito.getMonto(),
                        credito.getFechaInicio(),
                        credito.getFechaFin(),
                        credito.getEstado(),
                        credito.getTipoCredito(),
                        credito.getTipoInteres(),
                        credito.getPlazoGracia(),
                        credito.getNumeroDiasCuota(),
                        credito.getCliente().getIdCliente()))
                .collect(Collectors.toList());
    }
    

    //! ELIMINAR CREDITO
    public ResponseDTO eliminarCredito(Long idCredito) {
        Optional<Credito> creditoOpt = creditoRepository.findById(idCredito);
        if (creditoOpt.isPresent()) {
            creditoRepository.delete(creditoOpt.get());
            return new ResponseDTO("Crédito eliminado correctamente");
        }
        return new ResponseDTO("Crédito no encontrado");
    }
    // Calcular Valor Futuro Nominal (Interés Compuesto)
    private double calcularValorFuturoNominal(double principal, double timeInYears) {
        int m = DAYS_IN_YEAR; // Período de capitalización (diaria)
        double tn = ANNUAL_INTEREST_RATE; // Tasa Nominal (10% anual)
        double n = timeInYears * DAYS_IN_YEAR; // Tiempo transcurrido en días
        return principal * Math.pow(1 + (tn / m), n);
    }
    // Calcular Valor Futuro con Tasa Efectiva
    private double calcularValorFuturoEfectivo(double principal, double timeInYears) {
        double TEP = calcularTEP(timeInYears); // Tasa Efectiva del Periodo
        return principal * (1 + TEP); // Utiliza TEP directamente
    }
    // Calcular Tasa Efectiva del Periodo (TEP)
    private double calcularTEP(double timeInYears) {
        int m = DAYS_IN_YEAR; // Período de capitalización (diaria)
        double tn = ANNUAL_INTEREST_RATE; // Tasa Nominal (10% anual)
        double n = timeInYears * DAYS_IN_YEAR; // Tiempo transcurrido en días
        return Math.pow(1 + (tn / m), n) - 1;
    }
///?????
    // Calcular Renta sin Periodo de Gracia
    // private double rentaSinGracia(double principal, double tep, double n) {
    //     return principal * (tep * Math.pow(1 + tep, n)) / (Math.pow(1 + tep, n) - 1);
    // }
    // Calcular Valor Futuro sin Periodo de Gracia
    private double valorFuturoSinGracia(double renta, double tep, double n) {
        return renta * (Math.pow(1 + tep, n) - 1) / tep;
    }
  // Calcular Monto Acumulado durante el Periodo de Gracia
    // private double montoAcumuladoConGracia(double principal, double tep, int periodosDeGracia) {
    //     return principal * Math.pow(1 + tep, periodosDeGracia);
    // }
    // Calcular Renta con Periodo de Gracia
    // private double rentaConGracia(double principal, double tep, double n) {
    //     return principal * (tep * Math.pow(1 + tep, n)) / (Math.pow(1 + tep, n) - 1);
    // }
    // Calcular Valor Futuro con Periodo de Gracia
    private double valorFuturoConGracia(double renta, double tep, double n) {
        return renta * (Math.pow(1 + tep, n) - 1) / tep;
    }

    private double calcularRentaFrancesa(double principal, double tep, int numeroPeriodos) {
        return (principal * tep * Math.pow(1 + tep, numeroPeriodos)) / (Math.pow(1 + tep, numeroPeriodos) - 1);
    }
    // Calcular Tasa Efectiva del Periodo (TEP)
    private double calculoTEP(int diasPorPeriodo) {
        return Math.pow(1 + (ANNUAL_INTEREST_RATE / DAYS_IN_YEAR), diasPorPeriodo) - 1;
    }
    
}
