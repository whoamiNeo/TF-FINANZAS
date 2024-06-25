package pe.edu.upc.TFFinanzas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upc.TFFinanzas.dtos.DetalleCreditoDTO;
import pe.edu.upc.TFFinanzas.dtos.auth.ResponseDTO;
import pe.edu.upc.TFFinanzas.entities.Credito;
//import pe.edu.upc.TFFinanzas.dtos.DetalleCreditoResponseDTO;
import pe.edu.upc.TFFinanzas.entities.DetalleCredito;
import pe.edu.upc.TFFinanzas.repositories.CreditoRepository;
import pe.edu.upc.TFFinanzas.repositories.DetalleCreditoRepository;
import java.util.Optional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetalleCreditoService {
    private final DetalleCreditoRepository detalleCreditoRepository;
    private final CreditoRepository creditoRepository;

    // public List<DetalleCreditoDTO> listarDetallesPorCredito(Long idCredito) {
    //     List<DetalleCredito> detalles = detalleCreditoRepository.findByCreditoIdCredito(idCredito);
    //     return detalles.stream()
    //             .map(detalle -> new DetalleCreditoDTO(
    //                     detalle.getIdDetalleCredito(),
    //                     detalle.getSaldoInicial(),
    //                     detalle.getInteres(),
    //                     detalle.getRenta(),
    //                     detalle.getAmortizacion(),
    //                     detalle.getSaldoFinal(),
    //                     detalle.getFechaPagoCuota(),
    //                     detalle.isEstadoPago(),
    //                     detalle.getMora(),
    //                     detalle.getCredito().getIdCredito()))
    //             .collect(Collectors.toList());
    // }
    public List<DetalleCreditoDTO> listarDetallesPorCredito(Long idCredito) {
        List<DetalleCredito> detalles = detalleCreditoRepository.findByCreditoIdCredito(idCredito);
        return detalles.stream()
                .map(detalle -> {
                    // double mora = 0.0;
                    // if (LocalDate.now().isAfter(detalle.getFechaPagoCuota()) && !detalle.isEstadoPago()) {
                    //     long diasDeRetraso = ChronoUnit.DAYS.between(detalle.getFechaPagoCuota(), LocalDate.now());
                    //     mora = detalle.getRenta() * 0.10 * diasDeRetraso; // Aplicar un 10% por cada día de retraso
                    // }
                    return new DetalleCreditoDTO(
                            detalle.getIdDetalleCredito(),
                            detalle.getSaldoInicial(),
                            detalle.getInteres(),
                            detalle.getRenta(),
                            detalle.getAmortizacion(),
                            detalle.getSaldoFinal(),
                            detalle.getFechaPagoCuota(),
                            detalle.isEstadoPago(),
                            detalle.getMora(), // Incluir la mora en la respuesta
                            detalle.getCredito().getIdCredito());
                })
                .collect(Collectors.toList());
    }


    public ResponseDTO actualizarEstadoDetalleCredito(Long idDetalleCredito, boolean estadoPago) {
    Optional<DetalleCredito> detalleOpt = detalleCreditoRepository.findById(idDetalleCredito);
    if (detalleOpt.isPresent()) {
        DetalleCredito detalleCredito = detalleOpt.get();
        detalleCredito.setEstadoPago(estadoPago);
        detalleCreditoRepository.save(detalleCredito);
        actualizarEstadoCredito(detalleCredito.getCredito().getIdCredito());
        return new ResponseDTO("Estado de Detalle de Crédito actualizado correctamente");
    }
    return new ResponseDTO("Detalle de Crédito no encontrado");
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
}
