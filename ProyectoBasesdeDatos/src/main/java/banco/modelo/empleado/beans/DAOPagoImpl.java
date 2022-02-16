package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.utils.Fechas;


public class DAOPagoImpl implements DAOPago {

	private static Logger logger = LoggerFactory.getLogger(DAOPagoImpl.class);
	
	private Connection conexion;
	
	public DAOPagoImpl(Connection c) {
		this.conexion = c;
	}

	@Override
	public ArrayList<PagoBean> recuperarPagos(int nroPrestamo) throws Exception {
		logger.info("Inicia la recuperacion de los pagos del prestamo {}", nroPrestamo);
		
		/** 
		 * TODO HECHO Recupera todos los pagos del prestamo (pagos e impagos) del prestamo nroPrestamo
		 * 	    Si ocurre algún error deberá propagar una excepción.
		 */
		ArrayList<PagoBean> listaRetorno = new ArrayList<PagoBean>();
		try {
			java.sql.Statement stmt = this.conexion.createStatement();
			String sql = "SELECT * FROM pago AS p WHERE (p.nro_prestamo="+nroPrestamo+")";
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				PagoBean fila = new PagoBeanImpl();
				fila.setNroPrestamo(rs.getInt("nro_prestamo"));
				fila.setNroPago(rs.getInt("nro_pago"));
				fila.setFechaVencimiento(rs.getDate("fecha_venc"));
				fila.setFechaPago(rs.getDate("fecha_pago"));
				listaRetorno.add(fila);
			}
			rs.close(); // libera los recursos
			stmt.close(); // cierra la conexion y libera los recursos utilizados
		} catch (java.sql.SQLException E) {
			logger.error("ERROR SQL: "+E.getMessage());
			throw new Exception("Error en recuperar el pago del prestamo "+nroPrestamo);
		}
		return listaRetorno;
	}

	@Override
	public void registrarPagos(int nroCliente, int nroPrestamo, List<Integer> cuotasAPagar)  throws Exception {
		logger.info("Inicia el pago de las {} cuotas del prestamo {}", cuotasAPagar.size(), nroPrestamo);
		/**
		 * TODO HECHO Registra los pagos de cuotas definidos en cuotasAPagar.
		 * 
		 * nroCliente asume que esta validado
		 * nroPrestamo asume que está validado
		 * cuotasAPagar Debe verificar que las cuotas a pagar no estén pagas (fecha_pago = NULL)
		 * @throws Exception Si hubo error en la conexión
		 */		
		java.sql.Statement stmt = this.conexion.createStatement();
        for (Integer pago : cuotasAPagar) {
            String sql = "call registrarPago(" + nroPrestamo + "," + pago + ")";
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                if (!rs.getString(1).equals("Pago Exitoso")) {
                    throw new Exception(rs.getString(1));
                }
            } else {
                throw new Exception("ERROR procedimiento pagarCuota no retorna resultado");
            }
        }
	}
}