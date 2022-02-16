package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.utils.Fechas;

public class DAOPrestamoImpl implements DAOPrestamo {

	private static Logger logger = LoggerFactory.getLogger(DAOPrestamoImpl.class);
	
	private Connection conexion;
	
	public DAOPrestamoImpl(Connection c) {
		this.conexion = c;
	}
	
	@Override
	public void crearActualizarPrestamo(PrestamoBean prestamo) throws Exception {
		/** 
		 * TODO HECHO Crear o actualizar el Prestamo segun el PrestamoBean prestamo. 
		 *      Si prestamo tiene nroPrestamo es una actualizacion, si el nroPrestamo es null entonces es un nuevo prestamo.
		 * 
		 * @throws Exception deberá propagar la excepción si ocurre alguna. Puede capturarla para loguear los errores, ej.
		 *				logger.error("SQLException: " + ex.getMessage());
		 * 				logger.error("SQLState: " + ex.getSQLState());
		 *				logger.error("VendorError: " + ex.getErrorCode());
		 *		   pero luego deberá propagarla para que se encargue el controlador. 
		 */
		logger.info("Creación o actualizacion del prestamo.");
		logger.debug("meses : {}", prestamo.getCantidadMeses());
		logger.debug("monto : {}", prestamo.getMonto());
		logger.debug("tasa : {}", prestamo.getTasaInteres());
		logger.debug("interes : {}", prestamo.getInteres());
		logger.debug("cuota : {}", prestamo.getValorCuota());
		logger.debug("legajo : {}", prestamo.getLegajo());
		logger.debug("cliente : {}", prestamo.getNroCliente());
		/**
		 * Crea o actualiza el Prestamo segun el Bean. 
		 * 
		 *  Si el bean tiene nroPrestamo es una actualizacion. Si el nro de prestamo es null es un nuevo prestamo.
		 * 
		 * @throws Exception
		 */
		try {
			java.sql.Statement stmt = conexion.createStatement();
			String sql=null;
			if (prestamo.getNroPrestamo() == null) { // prestamo nuevo
				logger.debug("Crea un prestamo nuevo");
				sql = "INSERT INTO Prestamo (fecha, cant_meses, monto, tasa_interes, interes, "
						+ "valor_cuota, legajo, nro_cliente) VALUES (CURDATE(),"
						+prestamo.getCantidadMeses()+","+prestamo.getMonto()+","
						+prestamo.getTasaInteres()+","+prestamo.getInteres()+","
						+prestamo.getValorCuota()+","+prestamo.getLegajo()+","+prestamo.getNroCliente()+")";
			} else {// actualizacion
				logger.debug("Se actualiza el prestamo {}",prestamo.getNroPrestamo());
				sql = "UPDATE Prestamo SET fecha=CURDATE(), cant_meses="+prestamo.getCantidadMeses()+", "
					+ "monto="+prestamo.getMonto()+", tasa_interes= "+prestamo.getTasaInteres()+", "
					+ "interes="+prestamo.getInteres()+ ", valor_cuota= "+prestamo.getValorCuota()+","
					+ "legajo= "+prestamo.getLegajo()+", nro_cliente = "+prestamo.getNroCliente()
					+ "WHERE nro_prestamo="+prestamo.getNroPrestamo();
			}
			stmt.execute(sql);
			stmt.close();
		} catch (java.sql.SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());
			throw new Exception("Hubo un error en la creacion o actualizacion del prestamo.");
		}
	}

	@Override
	public PrestamoBean recuperarPrestamo(int nroPrestamo) throws Exception {
		logger.info("Recupera el prestamo nro {}.", nroPrestamo);
		/**
		 * TODO HECHO Obtiene el prestamo según el id nroPrestamo
		 * 
		 * @param nroPrestamo
		 * @return Un prestamo que corresponde a ese id o null
		 * @throws Exception si hubo algun problema de conexión
		 */		
		PrestamoBean prestamo = new PrestamoBeanImpl();
		try {
			java.sql.Statement stmt = this.conexion.createStatement();
			String sql = "SELECT * " + 
					" FROM prestamo AS p " + 
					" WHERE (p.nro_prestamo="+nroPrestamo+")";
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				prestamo.setNroPrestamo(rs.getInt("nro_prestamo"));
				prestamo.setFecha(rs.getDate("fecha"));
				prestamo.setCantidadMeses(rs.getInt("cant_meses"));
				prestamo.setMonto(rs.getDouble("monto"));
				prestamo.setTasaInteres(rs.getDouble("tasa_interes"));
				prestamo.setInteres(rs.getDouble("interes"));
				prestamo.setValorCuota(rs.getDouble("valor_cuota"));
				prestamo.setLegajo(rs.getInt("legajo"));
				prestamo.setNroCliente(rs.getInt("nro_cliente"));
			}
			rs.close(); // libera los recursos
			stmt.close(); // cierra la conexion y libera los recursos utilizados
		} catch (java.sql.SQLException E) {
			logger.error("ERROR SQL: "+E.getMessage());
			throw new Exception("Error en recuperar el prestamo.");
		}
		return prestamo;
	}

}
