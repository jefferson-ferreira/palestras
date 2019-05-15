package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DataUtil {
	
	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	/**
	 * Método que fomata uma Data em String no formato "dd/MM/yyyy".
	 * 
	 * Ex.: formataData(data);
	 * 
	 * @param data
	 * @return String - Data formatada "dd/MM/yyyy".
	 * @throws Exception
	 */
	public String formataData(Date data) throws Exception {   
		try {
			TimeZone gmtZone = TimeZone.getTimeZone("GMT");
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			format.setTimeZone(gmtZone);
			return format.format(data); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Não foi possível formatar a data";
	}  
	
	@SuppressWarnings("unused")
	public String data(int dia, int mes, int ano){
		GregorianCalendar cl = new GregorianCalendar();  
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");    
		Date data = new Date();
		cl.setTime(data);

		//Pega a dia
		int dateAtual = cl.get(GregorianCalendar.DATE) + dia;    
		cl.set(GregorianCalendar.DATE, dateAtual);   
		Date dataAtual = cl.getTime();

		//Pega o Mês
		int month = cl.get(GregorianCalendar.MONTH) + mes;
		cl.set(GregorianCalendar.MONTH, mes);

		//Pega o ano
		int year = cl.get(GregorianCalendar.YEAR) + ano;
		cl.set(GregorianCalendar.YEAR, ano);

		return dateFormat.format(dataAtual);
	}
	
	@SuppressWarnings("unused")
	public String dataSemFormatacao(int dia, int mes, int ano){
		GregorianCalendar cl = new GregorianCalendar();  
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");    
		Date data = new Date();
		cl.setTime(data);

		//Pega a dia
		int dateAtual = cl.get(GregorianCalendar.DATE) + dia;    
		cl.set(GregorianCalendar.DATE, dateAtual);   
		Date dataAtual = cl.getTime();

		//Pega o Mês
		int month = cl.get(GregorianCalendar.MONTH) + mes;
		cl.set(GregorianCalendar.MONTH, mes);

		//Pega o ano
		int year = cl.get(GregorianCalendar.YEAR) + ano;
		cl.set(GregorianCalendar.YEAR, ano);

		return dateFormat.format(dataAtual);
	}
	
}
