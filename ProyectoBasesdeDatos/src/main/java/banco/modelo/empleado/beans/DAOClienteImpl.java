package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.modelo.atm.ModeloATM;
import banco.utils.Fechas;

public class DAOClienteImpl implements DAOCliente {

	private static Logger logger = LoggerFactory.getLogger(DAOClienteImpl.class);
	
	private Connection conexion;
	
	public DAOClienteImpl(Connection c) {
		this.conexion = c;
	}
	
	@Override
	public ClienteBean recuperarCliente(String tipoDoc, int nroDoc) throws Exception {
		logger.info("recupera el cliente con documento de tipo {} y nro {}.", tipoDoc, nroDoc);
		/**
		 * TODO HECHO Recuperar el cliente que tenga un documento que se corresponda con los parámetros recibidos.  
		 *		Deberá generar o propagar una excepción si no existe dicho cliente o hay un error de conexión.		
		 */
		ClienteBean clienteRetorno = new ClienteBeanImpl();
		try {
			java.sql.Statement stmt = this.conexion.createStatement();
			String sql = "SELECT * " + 
					" FROM cliente AS c" + 
					" WHERE (c.tipo_doc='"+tipoDoc+"' AND c.nro_doc="+nroDoc+")";
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				clienteRetorno.setNroCliente(rs.getInt("nro_cliente"));
				clienteRetorno.setApellido(rs.getString("apellido"));
				clienteRetorno.setNombre(rs.getString("nombre"));
				clienteRetorno.setTipoDocumento(rs.getString("tipo_doc"));
				clienteRetorno.setNroDocumento(rs.getInt("nro_doc"));
				clienteRetorno.setDireccion(rs.getString("direccion"));
				clienteRetorno.setTelefono(rs.getString("telefono"));
				clienteRetorno.setFechaNacimiento(rs.getDate("fecha_nac"));
			}else {
				throw new Exception("No se encontro el cliente con numero de documento "+nroDoc);
			}
			rs.close(); // libera los recursos
			stmt.close(); // cierra la conexion y libera los recursos utilizados
		} catch (java.sql.SQLException E) {
			logger.error("ERROR SQL: "+E.getMessage());
			throw new Exception("Error en recuperar el cliente.");
		}
		return clienteRetorno;		
	}

	@Override
	public ClienteBean recuperarCliente(Integer nroCliente) throws Exception {
		logger.info("recupera el cliente por nro de cliente.");
		
		/**
		 * TODO HECHO Recuperar el cliente que tenga un número de cliente de acuerdo al parámetro recibido.  
		 *		Deberá generar o propagar una excepción si no existe dicho cliente o hay un error de conexión.		
		 */
		ClienteBean clienteRetorno = new ClienteBeanImpl();
		try {
			java.sql.Statement stmt = this.conexion.createStatement();
			String sql = "SELECT * " + 
					" FROM cliente AS c" + 
					" WHERE (c.nro_cliente="+nroCliente+")";
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				clienteRetorno.setNroCliente(rs.getInt("nro_cliente"));
				clienteRetorno.setApellido(rs.getString("apellido"));
				clienteRetorno.setNombre(rs.getString("nombre"));
				clienteRetorno.setTipoDocumento(rs.getString("tipo_doc"));
				clienteRetorno.setNroDocumento(rs.getInt("nro_doc"));
				clienteRetorno.setDireccion(rs.getString("direccion"));
				clienteRetorno.setTelefono(rs.getString("telefono"));
				clienteRetorno.setFechaNacimiento(rs.getDate("fecha_nac"));
			}else {
				throw new Exception("No se encontro el cliente con numero "+nroCliente);
			}
			rs.close(); // libera los recursos
			stmt.close(); // cierra la conexion y libera los recursos utilizados
		} catch (java.sql.SQLException E) {
			logger.error("ERROR SQL: "+E.getMessage());
			throw new Exception("Error en recuperar el cliente.");
		}
		return clienteRetorno;	

	}

}
