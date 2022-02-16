package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAOEmpleadoImpl implements DAOEmpleado {

	private static Logger logger = LoggerFactory.getLogger(DAOEmpleadoImpl.class);
	
	private Connection conexion;
	
	public DAOEmpleadoImpl(Connection c) {
		this.conexion = c;
	}

	@Override
	public EmpleadoBean recuperarEmpleado(int legajo) throws Exception {
		logger.info("recupera el empleado que corresponde al legajo {}.", legajo);
		/**
		 * TODO HECHO Debe recuperar los datos del empleado que corresponda al legajo pasado como parámetro.
		 *      Si no existe deberá retornar null y 
		 *      De ocurre algun error deberá generar una excepción.		 * 
		 */				
		EmpleadoBean empleado = new EmpleadoBeanImpl();
		try {
			java.sql.Statement stmt = this.conexion.createStatement();
			String sql = "SELECT * " + 
					" FROM empleado AS e " + 
					" WHERE (e.legajo="+legajo+")";
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				empleado.setLegajo(rs.getInt("legajo"));
				empleado.setApellido(rs.getString("apellido"));
				empleado.setNombre(rs.getString("nombre"));
				empleado.setTipoDocumento(rs.getString("tipo_doc"));
				empleado.setNroDocumento(rs.getInt("nro_doc"));
				empleado.setDireccion(rs.getString("direccion"));
				empleado.setTelefono(rs.getString("telefono"));
				empleado.setCargo(rs.getString("cargo"));
				empleado.setPassword(rs.getString("password")); // select md5(9);
				empleado.setNroSucursal(rs.getInt("nro_suc"));
			}
			rs.close(); // libera los recursos
			stmt.close(); // cierra la conexion y libera los recursos utilizados
		} catch (java.sql.SQLException E) {
			logger.error("ERROR SQL: "+E.getMessage());
			throw new Exception("Error en recuperar el empleado.");
		}
		return empleado;
	}

}
