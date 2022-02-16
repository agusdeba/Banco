package banco.modelo.atm;

import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.modelo.ModeloImpl;
import banco.utils.Fechas;

public class ModeloATMImpl extends ModeloImpl implements ModeloATM {

	private static Logger logger = LoggerFactory.getLogger(ModeloATMImpl.class);

	private String tarjeta = null; // mantiene la tarjeta del cliente actual
	private Integer codigoATM = null;

	/*
	 * La información del cajero ATM se recupera del archivo que se encuentra
	 * definido en ModeloATM.CONFIG
	 */
	public ModeloATMImpl() {
		logger.debug("Se crea el modelo ATM.");

		logger.debug("Recuperación de la información sobre el cajero");

		Properties prop = new Properties();
		try (FileInputStream file = new FileInputStream(ModeloATM.CONFIG)) {
			logger.debug("Se intenta leer el archivo de propiedades {}", ModeloATM.CONFIG);
			prop.load(file);

			codigoATM = Integer.valueOf(prop.getProperty("atm.codigo.cajero"));

			logger.debug("Código cajero ATM: {}", codigoATM);
		} catch (Exception ex) {
			logger.error("Se produjo un error al recuperar el archivo de propiedades {}.", ModeloATM.CONFIG);
		}
		return;
	}

	@Override
	public boolean autenticarUsuarioAplicacion(String tarjeta, String pin) throws Exception { 

		logger.info("Se intenta autenticar la tarjeta {} con pin {}", tarjeta, pin);
		boolean exito = false;
		/** 
		 * TODO HECHO Código que autentica que exista una tarjeta con ese pin (el pin guardado
		 * en la BD está en MD5) En caso exitoso deberá registrar la tarjeta en la
		 * propiedad tarjeta y retornar true. Si la autenticación no es exitosa porque
		 * no coincide el pin o la tarjeta no existe deberá retornar falso y si hubo
		 * algún otro error deberá producir una excepción.
		 */
		try {
			java.sql.Statement stmt = this.conexion.createStatement();
			String sql = "SELECT * " + " FROM tarjeta " + " WHERE nro_tarjeta = " + tarjeta + " AND PIN = MD5('" + pin
					+ "')";
			// Se ejecuta la sentencia y se recibe un resultado
			java.sql.ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				exito = true;
				this.tarjeta = tarjeta;
			}
			rs.close(); // libera los recursos
			stmt.close();
		} catch (Exception E) {
			throw new Exception(""); 
		}
		return exito; 
	}

	@Override
	public Double obtenerSaldo() throws Exception {
		logger.info("Se intenta obtener el saldo de cliente {}", 3);
		Double saldo = (double) 0;
		if (this.tarjeta == null) {
			throw new Exception("El cliente no ingresó la tarjeta");
		}
		/**
		 *  
		 * TODO HECHO Obtiene el saldo. Debe capturar la excepción SQLException y
		 * propagar una Exception más amigable.
		 */
		try {
			java.sql.Statement stmt = this.conexion.createStatement();
			// Se prepara el string SQL de la consulta
			String sql = "SELECT DISTINCT saldo " + " FROM tarjeta AS t JOIN trans_cajas_ahorro AS ca ON ("
					+ this.tarjeta + "= ca.nro_ca) ";
			// Se ejecuta la sentencia y se recibe un resultado
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			// Se recorre el resultado
			if (rs.next()) { // En caso de que haya una fila
				saldo = rs.getDouble("saldo"); // Obtiene el valor de la columna saldo y lo almacena en la variable saldo
			}
			rs.close(); // libera los recursos
			stmt.close(); // cierra la conexion y libera los recursos utilizados
		} catch (java.sql.SQLException E) {
			logger.error("ERROR SQL");
			throw new Exception("Error en obtener el saldo.");
		}
		return saldo;
	}

	@Override
	public ArrayList<TransaccionCajaAhorroBean> cargarUltimosMovimientos() throws Exception {
		return this.cargarUltimosMovimientos(ModeloATM.ULTIMOS_MOVIMIENTOS_CANTIDAD);
	}

	@Override
	public ArrayList<TransaccionCajaAhorroBean> cargarUltimosMovimientos(int cantidad) throws Exception {
		logger.info("Busca las ultimas {} transacciones en la BD de la tarjeta {}", cantidad,
				Integer.valueOf(this.tarjeta.trim()));

		/**
		 * TODO HECHO Deberá recuperar los ultimos movimientos del cliente, la cantidad
		 * está definida en el parámetro. Debe capturar la excepción SQLException y
		 * propagar una Exception más amigable.
		 */
		ArrayList<TransaccionCajaAhorroBean> listaRetorno = new ArrayList<TransaccionCajaAhorroBean>();

		try {
			// Se crea una sentencia jdbc para realizar la consulta
			java.sql.Statement stmt = this.conexion.createStatement();
			// Se prepara el string SQL de la consulta
			String sql = "SELECT fecha, hora , tipo, monto, cod_caja, destino "
					   +" FROM tarjeta AS t JOIN trans_cajas_ahorro AS ca ON (t.nro_ca = ca.nro_ca) "
					   +" WHERE (t.nro_tarjeta = " + tarjeta + ")" + " ORDER BY fecha DESC, hora DESC"
					   		+ " LIMIT "+cantidad;

			java.sql.ResultSet rs = stmt.executeQuery(sql);
			java.util.Date fecha;
			String tipoMov;

			while(rs.next()) { 
				TransaccionCajaAhorroBean tActual = new TransaccionCajaAhorroBeanImpl();
				fecha = Fechas.convertirStringADate(rs.getString("fecha"), rs.getString("hora"));
				tActual.setTransaccionFechaHora(fecha);
				tipoMov = rs.getString("tipo");
				if (tipoMov.equals("transferencia") || tipoMov.equals("extraccion") || tipoMov.equals("debito")) {
					tActual.setTransaccionMonto(-1 * rs.getDouble("monto"));
				} else {
					tActual.setTransaccionMonto(rs.getDouble("monto"));
				}
				tActual.setTransaccionTipo(tipoMov);
				tActual.setTransaccionCodigoCaja(rs.getInt("cod_caja"));
				tActual.setCajaAhorroDestinoNumero(rs.getInt("destino"));
				listaRetorno.add(tActual);
			}

			rs.close();
			stmt.close();

		} catch (java.sql.SQLException ex) {
			logger.error("ERROR SQL: "+ex.getMessage());
			throw new Exception("Error en cargar los ultimos movimientos.");
		}

		return listaRetorno;
	}

	@Override
	public ArrayList<TransaccionCajaAhorroBean> cargarMovimientosPorPeriodo(Date desde, Date hasta) throws Exception {
		/**
		 * TODO HECHO Deberá recuperar los ultimos del cliente que se han realizado entre las
		 * fechas indicadas. Debe capturar la excepción SQLException y propagar una
		 * Exception más amigable. Debe generar excepción sin las fechas son erroneas
		 * (ver descripción en interface)
		 */
		ArrayList<TransaccionCajaAhorroBean> listaRetorno = new ArrayList<TransaccionCajaAhorroBean>();
		try {
			String sDesde = Fechas.convertirDateAStringDB(desde);
			String sHasta = Fechas.convertirDateAStringDB(hasta);
			// Se crea una sentencia jdbc para realizar la consulta
			java.sql.Statement stmt = this.conexion.createStatement();
			// Se prepara el string SQL de la consulta
			String sql = "SELECT fecha, hora , tipo, monto, cod_caja, destino "
					+ " FROM tarjeta AS t JOIN trans_cajas_ahorro AS ca ON (t.nro_ca = ca.nro_ca) "
					+ " WHERE (t.nro_tarjeta = " + tarjeta + " AND fecha>='"+sDesde+"' AND fecha<='"+sHasta+"') " 
					+ " ORDER BY fecha DESC, hora DESC ";
			
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			java.util.Date fecha;
			String tipoMov;

			while (rs.next()) {
				TransaccionCajaAhorroBean tActual = new TransaccionCajaAhorroBeanImpl();
				fecha = Fechas.convertirStringADate(rs.getString("fecha"), rs.getString("hora"));
				tActual.setTransaccionFechaHora(fecha);
				tipoMov = rs.getString("tipo");
				if (tipoMov.equals("transferencia") || tipoMov.equals("extraccion") || tipoMov.equals("debito")) {
					tActual.setTransaccionMonto(-1 * rs.getDouble("monto"));
				} else {
					tActual.setTransaccionMonto(rs.getDouble("monto"));
				}
				tActual.setTransaccionTipo(tipoMov);
				tActual.setTransaccionCodigoCaja(rs.getInt("cod_caja"));
				tActual.setCajaAhorroDestinoNumero(rs.getInt("destino"));
				listaRetorno.add(tActual);
			}

			rs.close();
			stmt.close();

		} catch (java.sql.SQLException ex) {
			logger.error("ERROR SQL");
			throw new Exception("Error en cargar los ultimos movimientos por perido.");
		}
		logger.debug("Retorna una lista con {} elementos", listaRetorno.size());

		return listaRetorno;
	}

	@Override
	public Double extraer(Double monto) throws Exception {
		logger.info("Realiza la extraccion de ${} sobre la cuenta", monto);

		/** 
		 * TODO HECHO Deberá extraer de la cuenta del cliente el monto especificado (ya
		 * validado) y de obtener el saldo de la cuenta como resultado. Debe capturar la
		 * excepción SQLException y propagar una Exception más amigable. Debe generar
		 * excepción si las propiedades codigoATM o tarjeta no tienen valores
		 */
		if (this.codigoATM == null || this.tarjeta == null) {
			throw new Exception("El codigo o tarjeta no pueden tener valores vacios.");
		}
		try {
			java.sql.Statement stmt = this.conexion.createStatement();
			String sql = "call extraer("+monto+","+this.tarjeta+","+this.codigoATM+")";
			logger.debug(sql);
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			logger.debug("post executeQuery");
			
			if (rs.next() && !rs.getString(1).equals(ModeloATM.EXTRACCION_EXITOSA)) {
				throw new Exception("No es posible realizar la extraccion.");
			}
			rs.close(); // libera los recursos
			stmt.close(); // cierra la conexion y libera los recursos utilizados
		} catch (java.sql.SQLException E) {
			logger.error("ERROR SQL: "+E.getMessage());
			throw new Exception("Error en extraer.");
		}		
		return this.obtenerSaldo();
	}

	@Override
	public int parseCuenta(String p_cuenta) throws Exception {

		logger.info("Intenta realizar el parsing de un codigo de cuenta {}", p_cuenta);

		/** 
		 * TODO HECHO Verifica que el codigo de la cuenta sea valido. Debe capturar la
		 * excepción SQLException y propagar una Exception más amigable. Debe generar
		 * excepción si la cuenta es vacia, entero negativo o no puede generar el
		 * parsing. retorna la cuenta en formato int
		 */
		if (p_cuenta == null) {
			throw new Exception("El codigo de cuenta no puede ser vacío");
		}

		Integer cuenta = Integer.parseInt(p_cuenta);

		if (cuenta < 0 || cuenta == null) {
			throw new Exception("El codigo de la cuenta no es valido.");
		}
		try {
			java.sql.Statement stmt = this.conexion.createStatement();
			// Se prepara el string SQL de la consulta
			String sql = "SELECT * " + "FROM trans_cajas_ahorro AS ca" + " WHERE (ca.nro_ca = " + cuenta + ");";
			// Se ejecuta la sentencia y se recibe un resultado
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				logger.info("Encontró la cuenta en la BD.");
			}
			rs.close(); // libera los recursos
			stmt.close(); // cierra la conexion y libera los recursos utilizados
		} catch (java.sql.SQLException E) {
			logger.error("ERROR SQL");
			throw new Exception("Error en validar la cuenta.");
		}
		return cuenta;
	}

	@Override
	public Double transferir(Double monto, int cajaDestino) throws Exception {
		logger.info("Realiza la transferencia de ${} sobre a la cuenta {}", monto, cajaDestino);

		/**
		 * TODO HECHO Deberá extraer de la cuenta del cliente el monto especificado (ya
		 * validado) y de obtener el saldo de la cuenta como resultado. Debe capturar la
		 * excepción SQLException y propagar una Exception más amigable. Debe generar
		 * excepción si las propiedades codigoATM o tarjeta no tienen valores
		 */
		
		if (this.codigoATM == null || this.tarjeta == null) {
			throw new Exception("El codigo o tarjeta no pueden tener valores vacios.");
		}
		try {
			java.sql.Statement stmt = this.conexion.createStatement();
			String sql = "call transferir("+monto+","+this.tarjeta+","+this.codigoATM+","+cajaDestino+")";
			logger.debug(sql);
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			
			if (rs.next() && !rs.getString(1).equals(ModeloATM.TRANSFERENCIA_EXITOSA)) {
				throw new Exception("No es posible realizar la transferencia.");
			}
			rs.close(); // libera los recursos
			stmt.close(); // cierra la conexion y libera los recursos utilizados
		} catch (java.sql.SQLException E) {
			logger.error("ERROR SQL: "+E.getMessage());
			throw new Exception("Error en transferir.");
		}
		return this.obtenerSaldo();
	}

	@Override
	public Double parseMonto(String p_monto) throws Exception {

		logger.info("Intenta realizar el parsing del monto {}", p_monto);

		if (p_monto == null) {
			throw new Exception("El monto no puede estar vacío");
		}

		try {
			double monto = Double.parseDouble(p_monto);
			DecimalFormat df = new DecimalFormat("#.00");

			monto = Double.parseDouble(corregirComa(df.format(monto)));

			if (monto < 0) {
				throw new Exception("El monto no debe ser negativo.");
			}

			return monto;
		} catch (NumberFormatException e) {
			throw new Exception("El monto no tiene un formato válido.");
		}
	}

	private String corregirComa(String n) {
		String toReturn = "";

		for (int i = 0; i < n.length(); i++) {
			if (n.charAt(i) == ',') {
				toReturn = toReturn + ".";
			} else {
				toReturn = toReturn + n.charAt(i);
			}
		}

		return toReturn;
	}

}
