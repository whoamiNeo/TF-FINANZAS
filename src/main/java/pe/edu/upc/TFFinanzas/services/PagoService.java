package pe.edu.upc.TFFinanzas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upc.TFFinanzas.dtos.PagoDTO;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;
import pe.edu.upc.TFFinanzas.entities.Credito;
import pe.edu.upc.TFFinanzas.entities.DetalleCredito;
import pe.edu.upc.TFFinanzas.entities.Pago;
import pe.edu.upc.TFFinanzas.repositories.DetalleCreditoRepository;
import pe.edu.upc.TFFinanzas.repositories.PagoRespository;
import pe.edu.upc.TFFinanzas.repositories.CreditoRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {
    private final PagoRespository pagoRepository;
    private final DetalleCreditoRepository detalleCreditoRepository;
    private final CreditoRepository creditoRepository;

    public ResponseDTO registrarPago(PagoDTO pagoDTO) {
        Optional<DetalleCredito> detalleCreditoOpt = detalleCreditoRepository.findById(pagoDTO.getIdDetalleCredito());
        if (!detalleCreditoOpt.isPresent()) {
            return new ResponseDTO("Detalle de crédito no encontrado");
        }
        DetalleCredito detalleCredito = detalleCreditoOpt.get();
        LocalDate fechaPago = pagoDTO.getFechaPago();
        LocalDate fechaPagoCuota = detalleCredito.getFechaPagoCuota();
        double renta = detalleCredito.getRenta();
        double mora = 0.0;

        if (fechaPago.isAfter(fechaPagoCuota)) {
            long diasDeRetraso = ChronoUnit.DAYS.between(fechaPagoCuota, fechaPago);
            mora = renta * 0.10 * diasDeRetraso; // Aplicar un 10% por cada día de retraso
        }

        Pago pago = new Pago();
        pago.setMonto((float) (renta + mora)); // Sumar la mora al monto del pago
        pago.setFechaPago(fechaPago);
        pago.setTipoPago(pagoDTO.getTipoPago());
        pago.setEstadoPago(true); // Establecer el estado de pago a true por defecto
        pago.setDetalleCredito(detalleCredito);

        pagoRepository.save(pago);

        // Actualizar el estado de pago en DetalleCredito
        detalleCredito.setEstadoPago(true);
        detalleCredito.setMora(mora);; // Actualizar la mora en el detalle de crédito
        detalleCreditoRepository.save(detalleCredito);

        // Verificar si todos los detalles de crédito están pagados
        actualizarEstadoCredito(detalleCredito.getCredito().getIdCredito());

    return new ResponseDTO("Pago registrado correctamente");
    }

    private void actualizarEstadoCredito(Long idCredito) {
        List<DetalleCredito> detalles = detalleCreditoRepository.findByCreditoIdCredito(idCredito);
        boolean todosPagados = detalles.stream().allMatch(DetalleCredito::isEstadoPago);

        if (todosPagados) {
            Optional<Credito> creditoOpt = creditoRepository.findById(idCredito);
            if (creditoOpt.isPresent()) {
                Credito credito = creditoOpt.get();
                credito.setEstado(false); // Actualizar el estado del crédito a true
                creditoRepository.save(credito);
            }
        }
    }


     // Actualizar Pago
    public ResponseDTO actualizarPago(Long idPago, PagoDTO pagoDTO) {
        Optional<Pago> pagoOpt = pagoRepository.findById(idPago);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            pago.setMonto(pagoDTO.getMonto());
            pago.setFechaPago(pagoDTO.getFechaPago());
            pago.setTipoPago(pagoDTO.getTipoPago());
            pagoRepository.save(pago);
            return new ResponseDTO("Pago actualizado correctamente");
        }
        return new ResponseDTO("Pago no encontrado");
    }

    public List<PagoDTO> listarPagos() {
    List<Pago> pagos = pagoRepository.findAll();
    return pagos.stream()
        .map(pago -> new PagoDTO(
            pago.getIdPago(),
            pago.getMonto(),
            pago.getFechaPago(),
            pago.getTipoPago(),
            pago.isEstadoPago(),
            pago.getDetalleCredito().getIdDetalleCredito()))
             // Incluir el estado del pago en la respuesta
        .collect(Collectors.toList());
    }
    // Eliminar Pago
    public ResponseDTO eliminarPago(Long idPago) {
        Optional<Pago> pagoOpt = pagoRepository.findById(idPago);
        if (pagoOpt.isPresent()) {
            pagoRepository.delete(pagoOpt.get());
            return new ResponseDTO("Pago eliminado correctamente");
        }
        return new ResponseDTO("Pago no encontrado");
    }
}
