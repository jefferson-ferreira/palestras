package apresentacao_apresentacao;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import driver.AbstractCenario;
import massadedados.LerXls;

public class GenericType extends AbstractCenario {
	private static WebElement elemento;
	private static String elemento2;
	private static String elementoObsoleto;
	private static String navegarHTML;
	private static String tipoElemento;
	private static String valor;
	private static String valorEsperadoElemento;
	private static String valorRTA;
	private ExecucaoPassos executarPassos = new ExecucaoPassos();
	private LerXls xls = new LerXls();

	public static WebElement getElemento() {
		return elemento;
	}

	public static String getElemento2() {
		return elemento2;
	}

	public static String getElementoObsoleto() {
		return elementoObsoleto;
	}

	public static String getNavegarHTML() {
		return navegarHTML;
	}

	public static String getTipoElemento() {
		return tipoElemento;
	}

	public static String getValor() {
		return valor;
	}

	public static String getValorEsperadoElemento() {
		return valorEsperadoElemento;
	}

	public static String getValorRTA() {
		return valorRTA;
	}

	public static void setElemento(WebElement elemento) {
		GenericType.elemento = elemento;
	}

	public static void setElemento2(String elemento2) {
		GenericType.elemento2 = elemento2;
	}

	public static void setElementoObsoleto(String elementoObsoleto) {
		GenericType.elementoObsoleto = elementoObsoleto;
	}

	public static void setNavegarHTML(String navegarHTML) {
		GenericType.navegarHTML = navegarHTML;
	}

	public static void setTipoElemento(String tipoElemento) {
		GenericType.tipoElemento = tipoElemento;
	}

	public static void setValor(String valor) {
		GenericType.valor = valor;
	}

	public static void setValorEsperadoElemento(String valorEsperadoElemento) {
		GenericType.valorEsperadoElemento = valorEsperadoElemento;
	}

	public static void setValorRTA(String valorRTA) {
		GenericType.valorRTA = valorRTA;
	}

	public WebElement getElement(String tipoLocalizacaoElemento, String localizacaoElemento) {
		if(getTipoElemento().equals("mensagem")) {
			delay(4000);
		}
		switch (tipoLocalizacaoElemento) {
		case "xpath":
			aguardarElementoEstarPresente(webDriver.findElement(By.xpath(localizacaoElemento)), 3);
			return webDriver.findElement(By.xpath(localizacaoElemento));
		case "id":
			aguardarElementoEstarPresente(webDriver.findElement(By.id(localizacaoElemento)), 3);
			return webDriver.findElement(By.id(localizacaoElemento));
		case "css":
			aguardarElementoEstarPresente(webDriver.findElement(By.cssSelector(localizacaoElemento)), 3);
			return webDriver.findElement(By.cssSelector(localizacaoElemento));
		case "class":
			aguardarElementoEstarPresente(webDriver.findElement(By.className(localizacaoElemento)), 3);
			return webDriver.findElement(By.className(localizacaoElemento));
		case "name":
			aguardarElementoEstarPresente(webDriver.findElement(By.name(localizacaoElemento)), 3);
			return webDriver.findElement(By.name(localizacaoElemento));
		case "linkText":
			aguardarElementoEstarPresente(webDriver.findElement(By.linkText(localizacaoElemento)), 3);
			return webDriver.findElement(By.linkText(localizacaoElemento));
		case "tagName":
			aguardarElementoEstarPresente(webDriver.findElement(By.tagName(localizacaoElemento)), 3);
			return webDriver.findElement(By.tagName(localizacaoElemento));
		default:
			return null;
		}
	}

	public String getElement2(String tipoLocalizacaoElemento, String localizacaoElemento) {
		if (tipoLocalizacaoElemento.equals("outros")) {
			return localizacaoElemento;
		}
		return null;
	}

	private List<String> montarListaParaAlvo(ArrayList<String> arrayList) {
		List<String> alvos = new ArrayList<String>();
		for (String item : arrayList) {
			alvos.add(item);
		}
		return alvos;
	}

	public void valoresRTA(final ArrayList<String> arrayList) throws Exception {
		List<String> valorRTA = montarListaParaAlvo(arrayList);
		for (int i = 0; i < valorRTA.size(); i++) {
			String valor = xls.lerPlanilha(valorRTA.get(i));
			setValor(valor);
			if (xls.naox(valor)) {
				WebElement elemento = null;
				String elemento2 = null;
				setElemento2(elemento2);
				String tipoElemento = xls.lerPlanilhaDadosElementos(valorRTA.get(i), 5);
				setTipoElemento(tipoElemento);
				if (xls.lerPlanilhaDadosElementos(valorRTA.get(i), 7).equals("outros")) {
					elemento2 = getElement2(xls.lerPlanilhaDadosElementos(valorRTA.get(i), 7),
							xls.lerPlanilhaDadosElementos(valorRTA.get(i), 8));
					setElemento2(elemento2);
				} else {
					elemento = getElement(xls.lerPlanilhaDadosElementos(valorRTA.get(i), 7),
							xls.lerPlanilhaDadosElementos(valorRTA.get(i), 8));
					setElemento(elemento);
					setElementoObsoleto(xls.lerPlanilhaDadosElementos(valorRTA.get(i), 8));
				}
				setNavegarHTML(xls.lerPlanilhaDadosElementos(valorRTA.get(i), 9));
				setValorEsperadoElemento(xls.lerPlanilhaDadosElementos(valorRTA.get(i), LerXls.getLinhaCasoTeste()));
				setValorRTA(valorRTA.get(i).split("_")[0]);

				executarPassos.executar();

				proximoPath();
			}
		}
	}

	private void proximoPath() throws Exception{
		if(tipoElemento.equals("path")) {
			valoresRTA(xls.lerPlanilhaElementos());
		}
	}
}
