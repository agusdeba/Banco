package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAOClienteMorosoImpl implements DAOClienteMoroso {

	private static Logger logger = LoggerFactory.getLogger(DAOClienteMorosoImpl.class);
	
	private Connection conexion;
	
	public DAOClienteMorosoImpl(Connection c) {
		this.conexion = c;
	}
	
	@Override
	public ArrayList<ClienteMorosoBean> recuperarClientesMorosos() throws Exception {
		logger.info("Busca los clientes morosos.");
		DAOPrestamo daoPrestamo = new DAOPrestamoImpl(this.conexion);		
		DAOCliente daoCliente = new DAOClienteImpl(this.conexion);
		/**
		 * TODO HECHO Deberá recuperar un listado de clientes morosos los cuales consisten de un bean ClienteMorosoBeanImpl
		 *      deberá indicar para dicho cliente cual es el prestamo sobre el que está moroso y la cantidad de cuotas que 
		 *      tiene atrasadas. En todos los casos deberá generar excepciones que será capturadas por el controlador
		 *      si hay algún error que necesita ser informado al usuario. 
		 */
		ArrayList<ClienteMorosoBean> morosos = new ArrayList<ClienteMorosoBean>();
		PrestamoBean prestamo = null;
		ClienteBean cliente = null;
		ClienteMorosoBean clienteMoroso;
		try {
			java.sql.Statement stmt = this.conexion.createStatement();
			String sql = "SELECT * FROM "
					+    "  (SELECT pago.nro_prestamo, COUNT(pago.nro_prestamo) cuotasImpagas, C.tipo_doc, C.nro_doc "
					+    "   FROM ((pago JOIN prestamo AS prest ON (pago.nro_prestamo=prest.nro_prestamo))"
					+    "	      JOIN cliente AS C ON (prest.nro_cliente=C.nro_cliente))"
					+    "   WHERE (fecha_venc < CURDATE() AND fecha_pago)"
					+    "  is NULL GROUP BY pago.nro_prestamo) subres "
					+    "WHERE cuotasImpagas >= 2";
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				clienteMoroso = new ClienteMorosoBeanImpl();
				prestamo = daoPrestamo.recuperarPrestamo(rs.getInt("nro_prestamo"));
				cliente = daoCliente.recuperarCliente(rs.getString("tipo_doc"), rs.getInt("nro_doc"));
				clienteMoroso.setCliente(cliente);
				clienteMoroso.setPrestamo(prestamo);
				clienteMoroso.setCantidadCuotasAtrasadas(rs.getInt("cuotasImpagas"));
				morosos.add(clienteMoroso);
			}
			rs.close(); // libera los recursos
			stmt.close(); // cierra la conexion y libera los recursos utilizados
		} catch (java.sql.SQLException E) {
			logger.error("ERROR SQL: "+E.getMessage());
			throw new Exception("Error en recuperar los clientes morosos.");
		}
		return morosos;
	}
}