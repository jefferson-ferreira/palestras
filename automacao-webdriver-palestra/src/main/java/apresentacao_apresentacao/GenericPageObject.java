package apresentacao_apresentacao;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import driver.AbstractCenario;
import driver.GenericWebDriverSingleton;
import massadedados.LerXls;
import parametrizacao.MassaDadosUtil;
import parametrizacao.ParametroUtil;
import util.MensagemLog;
import util.MensagemLogManualUsuario;
import util.MensagemUtil;
import util.Relogio;
import util.TxtUtil;

@SuppressWarnings("unused")
public class GenericPageObject extends AbstractCenario{
	private MensagemLog log = new MensagemLog();
	private MensagemLogManualUsuario logManualUsuario = new MensagemLogManualUsuario();
	private MensagemUtil msg = new MensagemUtil();
	private LerXls xls = new LerXls();
	private static String habilitarColetaEvidenciasTeste = ParametroUtil.getValueAsString("coletaEvidenciasTeste");
	Actions action = new Actions(webDriver);


	/**
	 * Método para acessar o Menu.
	 * Ex.: acessarMenu("Administração->Grupo");
	 * 
	 * @param listaDeMenus
	 * @throws Exception 
	 * @throws IOException 
	 */

	public void acessarMenu() throws Exception{
		if(!AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Suite") ||
				Relogio.getNumeroCasosTesteAtual() == 1 || 
				AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Instalacao") ||
				AbstractCenario.getPosCondicao()){
			String listaDeMenus = (xls.lerPlanilha("Str_Menu"))+"->"+(xls.lerPlanilha("Str_Item_Menu"))+"->"+
					(xls.lerPlanilha("Str_Sub_Item_Menu"));
			if(!listaDeMenus.equals(getMenuAtual())){
				setMenuAtual(listaDeMenus);	
				try {
					if(!ParametroUtil.getValueAsBoolean("utilizaXls")){
						setPassoPasso(listaDeMenus);
					} 
					String[] menus = listaDeMenus.split("->");
					List<WebElement> menu;
					List<WebElement> itensMenu;
					List<WebElement> subItensMenu;

					((JavascriptExecutor)webDriver).executeScript("window.scrollTo(0,0);", 
							webDriver.findElements(By.xpath("//*[@id='menubarID']")));
					menu = webDriver.findElements(By.xpath("//*[@id='menubarID']/ul/li"));

					for (WebElement menuPrincipal : menu) {
						if(menuPrincipal.findElement(By.tagName("span")).getText().equals(menus[0])){
							mouseOverElement(menuPrincipal.findElement(By.tagName("span")));
							delay(200);
							log.clicouMenu(menus[0]);
							logManualUsuario.clicarMenu(menus[0]);

							itensMenu = menuPrincipal.findElements(By.xpath("ul/li"));

							for (WebElement itemMenu : itensMenu) {
								if(itemMenu.findElement(By.xpath("a/span")).getText().equals(menus[1])){
									if(menus[2].equals("x")){
										coletarEvidencia();
										itemMenu.findElement(By.xpath("a/span")).click();
										delay(200);
										coletarEvidencia();
										log.clicouSubMenu(menus[1]);
										logManualUsuario.clicarSubMenu(menus[1]);
										atualizarRelogio();
										return;
									}else {
										mouseOverElement(itemMenu.findElement(By.xpath("a/span")));
										delay(200);
										log.clicouSubMenu(menus[1]);
										logManualUsuario.clicarSubMenu(menus[1]);
										coletarEvidencia();

										subItensMenu = itemMenu.findElements(By.tagName("li")); 
										for (WebElement subItem : subItensMenu) {
											if(subItem.findElement(By.xpath("a/span")).getText().equals(menus[2])){
												subItem.findElement(By.xpath("a/span")).click();
												log.clicouItemSubMenu(menus[2]);
												logManualUsuario.clicarItemSubMenu(menus[2]);
												delay(450);
												coletarEvidencia();
												atualizarRelogio();
												return;
											}
										}
									}
								}
							}
						}
					}

				} catch (NoSuchElementException nsee) {
					throw new NoSuchElementException("Menu \"" + listaDeMenus
							+ "\" não encontrado ou o Frame central não foi apresentado.");
				}
				throw new NoSuchElementException("Menu \"" + listaDeMenus
						+ "\" não encontrado. Ou o Carregamento do elemento foi muito demorado.");
			}else {
				Relogio.incremetarNumeroCasosTesteAtual();
				Relogio.executarRelogio();
			}
		}else {
			Relogio.incremetarNumeroCasosTesteAtual();
			Relogio.executarRelogio();
		}
		delay(400);
	}

	public void frame() {
		if (GenericType.getElemento2() == null) {
			mudarFrame(GenericType.getElemento());
		} else {
			mudarFrameDefault();
		}
	}

	public void acoes() {
		String[] valores = GenericType.getValor().split(",");
		try {
			for (String valorTratado : valores) {
				if (valorTratado.toLowerCase().equals("f5")) {
					GenericType.getElemento().sendKeys(Keys.F5);
					delay(1000);
				} else if (valorTratado.toLowerCase().equals("tab")) {
					delay(300);
					webDriver.findElement(By.xpath(GenericType.getElementoObsoleto())).sendKeys(Keys.TAB);
					delay(300);
				} else if (valorTratado.toLowerCase().equals("click")) {
					delay(300);
					webDriver.findElement(By.xpath(GenericType.getElementoObsoleto())).click();
					delay(300);
				} else if (valorTratado.toLowerCase().equals("end")) {
					webDriver.findElement(By.xpath(GenericType.getElementoObsoleto())).sendKeys(Keys.END);
				} else if (valorTratado.toLowerCase().equals("setaparabaixo")) {
					delay(300);
					webDriver.findElement(By.xpath(GenericType.getElementoObsoleto())).sendKeys(Keys.ARROW_DOWN);
					delay(300);
				} else if (valorTratado.toLowerCase().equals("foco")) {
					delay(300);
					((JavascriptExecutor) webDriver).executeScript(
							"window.focus();", webDriver.findElement(By.xpath(GenericType.getElementoObsoleto())));
					delay(300);
				} else if (valorTratado.toLowerCase().equals("esc")) {
					webDriver.findElement(By.xpath(GenericType.getElementoObsoleto())).sendKeys(Keys.ESCAPE);
				}else {
					delay(Integer.parseInt(valorTratado));
				}

			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException();
		}
	}

	public void geraText() throws Exception {
		((JavascriptExecutor) webDriver).executeScript(
				"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
		if(recuperarDescricaoClasse().contains("Suite") && !AbstractCenario.getPosCondicao() && !AbstractCenario.getPreCondicao()) {
			VerifyApresentacao.assertEquals(
					msg.obterMsgComparacaoCampo(GenericType.getValorRTA(), recuperarCasoTeste()),
					GenericType.getValorEsperadoElemento(), GenericType.getElemento().getAttribute("value"));
			log.comparouCampo(GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", ""));
		}
		geraString(Integer.parseInt(GenericType.getValor()));
		log.informouCampoComParametro(GenericType.getValorRTA(), GenericType.getValor());
		logManualUsuario.informarCampoComParametro(GenericType.getValorRTA(), GenericType.getValor());
	}

	public void geraTextoCkeditorJavaScript() {
		((JavascriptExecutor) webDriver).executeScript(
				"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});",
				webDriver.findElement(By.xpath(GenericType.getNavegarHTML())));
		((JavascriptExecutor) webDriver)
		.executeScript("CKEDITOR.instances['" + GenericType.getElemento().getAttribute("id").split("_")[1]
				+ "'].setData('" + geraString(Integer.parseInt(GenericType.getValor())) + "')");
		log.informouCampoComParametro(GenericType.getValorRTA(), GenericType.getValor());
		logManualUsuario.informarCampoComParametro(GenericType.getValorRTA(), GenericType.getValor());
	}

	public void geraNumero() throws Exception {
		((JavascriptExecutor) webDriver).executeScript(
				"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
		if(recuperarDescricaoClasse().contains("Suite") && !AbstractCenario.getPosCondicao() && !AbstractCenario.getPreCondicao()) {
			VerifyApresentacao.assertEquals(
					msg.obterMsgComparacaoCampo(GenericType.getValorRTA(),
							recuperarCasoTeste()),
					GenericType.getValorEsperadoElemento(), GenericType.getElemento().getAttribute("value"));
			log.comparouCampo(GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", ""));
		}
		GenericType.getElemento().sendKeys(geraNumero(Integer.parseInt(GenericType.getValor())));
		log.informouCampoComParametro(GenericType.getValorRTA(), GenericType.getElemento().getAttribute("value"));
		logManualUsuario.informarCampoComParametro(GenericType.getValorRTA(), GenericType.getElemento().getAttribute("value"));
	}

	public void text() throws Exception {
		try {
			((JavascriptExecutor) webDriver).executeScript(
					"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
			if(recuperarDescricaoClasse().contains("Suite") && !AbstractCenario.getPosCondicao() && !AbstractCenario.getPreCondicao()) {
				VerifyApresentacao.assertEquals(
						msg.obterMsgComparacaoCampo(GenericType.getValorRTA(),
								recuperarCasoTeste()),
						GenericType.getValorEsperadoElemento(), GenericType.getElemento().getAttribute("value"));
				log.comparouCampo(GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", ""));
			}
			//if(!GenericType.getElemento().getAttribute("value").equals(GenericType.getValor())) {
			GenericType.getElemento().clear();
			GenericType.getElemento().sendKeys(GenericType.getValor());
			log.informouCampoComParametro(GenericType.getValorRTA(), GenericType.getValor());
			logManualUsuario.informarCampoComParametro(GenericType.getValorRTA(), GenericType.getValor());
			//}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
					GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", "")));
		}
	}

	public void label() throws Exception {
		try {
			((JavascriptExecutor) webDriver).executeScript(
					"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
			if(GenericType.getElemento().getAttribute("value") == null) {
				VerifyApresentacao.assertEquals(
						msg.obterMsgComparacaoCampo(GenericType.getValorRTA(), recuperarCasoTeste()),
						GenericType.getValor(), GenericType.getElemento().getText());
			}else {
				VerifyApresentacao.assertEquals(
						msg.obterMsgComparacaoCampo(GenericType.getValorRTA(),
								recuperarCasoTeste()),
						GenericType.getValor(), GenericType.getElemento().getAttribute("value"));
			}
			log.comparouCampo(GenericType.getValorRTA(), GenericType.getValor());
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
					GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", "")));
		}
	}

	public void upload() throws Exception {
		try {
			if (!GenericType.getValor().equals("")) {
				aguardarElementoEstarPresente(GenericType.getElemento(), 2);
				GenericType.getElemento().click();
				Pattern nome = new Pattern(ParametroUtil.getValueAsString("imagemNome"));
				Pattern abrir = new Pattern(ParametroUtil.getValueAsString("imagemAbrir"));

				Screen src = new Screen();
				src.setAutoWaitTimeout(30);
				delay(500);
				src.type(nome, GenericType.getValor());
				src.click(abrir);

				log.informouCampoComParametro(GenericType.getValorRTA(), GenericType.getValor());
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
					GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", "")));
		}
	}

	public void button() throws Exception {
		try {

			aguardarElementoEstarPresente(GenericType.getElemento(), 1);
			((JavascriptExecutor) webDriver).executeScript(
					"arguments[0].scrollIntoView({block: \"start\", behavior: \"auto\"});", GenericType.getElemento());
			coletarEvidencia();
			GenericType.getElemento().click();
			log.clicouBotaoComParametro(GenericType.getValorRTA(),
					xls.lerPlan().replace("Módulo: ", ""));
			logManualUsuario.clicarBotaoComParametro(GenericType.getValorRTA(),
					xls.lerPlan().replace("Módulo: ", ""));
			delay(4000);
			coletarEvidencia();
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoBotaoIncorreto(
					GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", "")));
		}
	}

	public void buttonList() throws Exception {
		String[] tagName = GenericType.getNavegarHTML().split("-");
		try {
			for (String valor2 : GenericType.getValor().split(",")) {
				aguardarElementoEstarPresente(GenericType.getElemento(), 1);
				((JavascriptExecutor) webDriver).executeScript(
						"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
				GenericType.getElemento().findElement(By.tagName(tagName[0])).click();
				try {
					boolean achou = false;
					for (int j = 0; j < 20; j++) {
						List<WebElement> gridItens = GenericType.getElemento().findElements(By.tagName(tagName[1]));
						for (WebElement itemGrid : gridItens) {
							if (achou)
								break;
							List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
							for (int k = 0; k < (linhaGrid.size()); k++) {
								if (tagName.length > 3) {
									linhaGrid = itemGrid.findElements(By.tagName(tagName[3]));
								}
								if (GenericType.getValor().equals(linhaGrid.get(k).getText())) {
									achou = true;
									linhaGrid.get(k).click();
									log.clicouBotaoComParametro(GenericType.getValorRTA(),
											xls.lerPlan().replace("Módulo: ", ""));
									logManualUsuario.clicarBotaoComParametro(GenericType.getValorRTA(),
											xls.lerPlan().replace("Módulo: ", ""));
									break;
								}
							}
						}
						if (achou)
							break;
					}
					if (!achou) {
						throw new Exception("O registro -> " + GenericType.getValor() + " <- não foi encontrado na lista.");
					}
				} catch (NoSuchElementException nsee) {
					throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoBotaoIncorreto(
					GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", "")));
		}
	}

	public void data() throws Exception {
		try {
			if (recuperarDescricaoClasse().contains("Suite")) {
				GenericType.getElemento().clear();
			}
			((JavascriptExecutor) webDriver).executeScript(
					"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
			if (GenericType.getValorEsperadoElemento().equals("D")) {
				GenericType.setValorEsperadoElemento(data(0, 0, 0));
			} else if (GenericType.getValorEsperadoElemento().contains("D")) {
				GenericType.setValorEsperadoElemento(data(
						Integer.parseInt(GenericType.getValorEsperadoElemento().replace("D", "").replace(" ", "")), 0, 0));
			}
			if(recuperarDescricaoClasse().contains("Suite") && !AbstractCenario.getPosCondicao() && !AbstractCenario.getPreCondicao()) {
				VerifyApresentacao.assertEquals(
						msg.obterMsgComparacaoCampo(GenericType.getValorRTA(),
								recuperarCasoTeste()),
						GenericType.getValorEsperadoElemento(), GenericType.getElemento().getAttribute("value"));
				log.comparouCampo(GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", ""));
			}
			if (!GenericType.getElemento().getAttribute("value").equals(GenericType.getValor())) {
				if (GenericType.getValor().equals("D")) {
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].value=('"+ data(0, 0, 0) +"')", GenericType.getElemento());
					log.informouCampoComParametro(GenericType.getValorRTA(), data(0, 0, 0));
					logManualUsuario.informarCampoComParametro(GenericType.getValorRTA(), data(0, 0, 0));
				} else if (GenericType.getValor().contains("D")) {
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].value=('"+ data(Integer.parseInt(GenericType.getValor().replace("D", "").replace(" ", "")), 0, 0) +"')", GenericType.getElemento());
					log.informouCampoComParametro(GenericType.getValorRTA(),
							data(Integer.parseInt(GenericType.getValor().replace("D", "").replace(" ", "")), 0, 0));
					logManualUsuario.informarCampoComParametro(GenericType.getValorRTA(),
							data(Integer.parseInt(GenericType.getValor().replace("D", "").replace(" ", "")), 0, 0));
				} else {
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].value=('"+ GenericType.getValor() +"')", GenericType.getElemento());
					log.informouCampoComParametro(GenericType.getValorRTA(), GenericType.getValor());
					logManualUsuario.informarCampoComParametro(GenericType.getValorRTA(), GenericType.getValor());
				}
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
					GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", "")));
		}
	}

	public void labelData() throws Exception {
		try {
			((JavascriptExecutor) webDriver).executeScript(
					"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
			if (GenericType.getValorEsperadoElemento().equals("D")) {
				GenericType.setValorEsperadoElemento(data(0, 0, 0));
			} else if (GenericType.getValorEsperadoElemento().contains("D")) {
				GenericType.setValorEsperadoElemento(data(
						Integer.parseInt(GenericType.getValorEsperadoElemento().replace("D", "").replace(" ", "")), 0, 0));
			}
			if(recuperarDescricaoClasse().contains("Suite") && !AbstractCenario.getPosCondicao() && !AbstractCenario.getPreCondicao()) {
				VerifyApresentacao.assertEquals(
						msg.obterMsgComparacaoCampo(GenericType.getValorRTA(),
								recuperarCasoTeste()),
						GenericType.getValorEsperadoElemento(), GenericType.getElemento().getText());
				log.comparouCampo(GenericType.getValorRTA(), data(Integer.parseInt(GenericType.getValor().replace("D", "").replace(" ", "")), 0, 0));
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
					GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", "")));
		}
	}

	public void click() throws Exception {
		try {
			coletarEvidencia();
			GenericType.getElemento().click();
			delay(300);
			coletarEvidencia();
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoBotaoIncorreto(
					GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", "")));
		}
	}

	public void arrastaSoltaGrid() throws Exception {
		aguardarElementoEstarPresente(GenericType.getElemento(), 5);
		String[] valores = GenericType.getValor().split(","); 
		String[] tagName = GenericType.getNavegarHTML().split("-");
		try {
			for (String valorTratado : valores) {
				boolean achou = false;
				for (int j = 0; j < 20; j++) {
					List<WebElement> gridItens = GenericType.getElemento().findElements(By.tagName(tagName[0]));
					for (WebElement itemGrid : gridItens) {
						if (achou)
							break;
						List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
						for (int k = 0; k < (linhaGrid.size()); k++) {
							((JavascriptExecutor) webDriver).executeScript(
									"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", linhaGrid.get(k));
							if (valorTratado.equals(linhaGrid.get(k).getText())) {
								int acao = linhaGrid.size();
								for (int p = 0; p < acao; p++) {
									achou = true;
									action.dragAndDrop(linhaGrid.get(k), webDriver.findElement(By.xpath(tagName[2]))).build().perform();
									delay(500);
									break;
								}
								break;
							}
						}
					}
					if (achou)
						break;
				}
				if (!achou) {
					throw new Exception("O registro -> " + valorTratado + " <- não foi encontrado na lista.");
				}
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void combo() throws Exception {
		try {
			((JavascriptExecutor) webDriver).executeScript(
					"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
			delay(500);
			VerifyApresentacao.assertEquals(
					msg.obterMsgComparacaoCombo(GenericType.getValorRTA().split("_")[0], recuperarCasoTeste()),
					GenericType.getValorEsperadoElemento(), getCombo(GenericType.getElemento()));
			log.comparouCombo(xls.lerPlan().replace("Módulo: ", ""), GenericType.getValorRTA().split("_")[0]);

			if (!GenericType.getValor().equals("") && !getCombo(GenericType.getElemento()).equals(GenericType.getValor())) {
				Select combo = new Select(GenericType.getElemento());
				combo.selectByVisibleText(GenericType.getValor());
				log.selecionouCombo(GenericType.getValor(), GenericType.getValorRTA().split("_")[0]);
				logManualUsuario.selecionarCombo(GenericType.getValor(), GenericType.getValorRTA().split("_")[0]);
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoComboIncorreto(
					xls.lerPlan().replace("Módulo: ", ""), GenericType.getValorRTA().split("_")[0]));

		}
	}

	public void check() throws Exception {
		String situacaoCheck = null;
		if(GenericType.getElemento().isSelected()) {
			situacaoCheck = "marcado";
		} else {
			situacaoCheck = "desmarcado";
		}
		try {
			((JavascriptExecutor) webDriver).executeScript(
					"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
			if(recuperarDescricaoClasse().contains("Suite") && !AbstractCenario.getPosCondicao() && !AbstractCenario.getPreCondicao()) {
				VerifyApresentacao.assertEquals(
						msg.obterMsgComparacaoCheck(GenericType.getValorRTA(), recuperarCasoTeste(), 
								xls.lerPlan().replace("Módulo: ", "")), GenericType.getValorEsperadoElemento().toLowerCase(), situacaoCheck);
				log.comparouCheck(xls.lerPlan().replace("Módulo: ", ""), GenericType.getValorRTA());
			}
			if(situacaoCheck.equals("desmarcado") && GenericType.getValor().toLowerCase().equals("marcar")) {
				GenericType.getElemento().click();
				log.marcouCheck(GenericType.getValorRTA());
				logManualUsuario.marcarCheck(GenericType.getValorRTA());
				delay(300);
			}else if(situacaoCheck.equals("marcado") && GenericType.getValor().toLowerCase().equals("desmarcar")){
				GenericType.getElemento().click();
				log.desmarcouCheck(GenericType.getValorRTA());
				logManualUsuario.desmarcarCheck(GenericType.getValorRTA());
				delay(300);
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(
					msg.obterMsgMapeamentoCheckIncorretoParametro(GenericType.getValorRTA()));
		}
	}

	public void checkGrid() throws Exception {
		((JavascriptExecutor) webDriver).executeScript(
				"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
		String[] tagName = GenericType.getNavegarHTML().split("-");
		String[] valores = GenericType.getValor().split(",");
		String[] check = GenericType.getValorEsperadoElemento().split(",");
		int achouTodos = 0;
		try {
			boolean proximaPagina = false;
			boolean achou = false;
			for (int j = 0; j < 20; j++) {
				List<WebElement> gridItens = webDriver.findElement(By.xpath(GenericType.getElementoObsoleto())).findElements(By.tagName(tagName[0]));
				for (WebElement itemGrid : gridItens) {
					if (achou)
						break;
					List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
					for (int k = Integer.parseInt(tagName[2])-1; k < (linhaGrid.size()-1); k++) {
						for (int r = 0; r < valores.length; r++) {
							if (valores[r].trim().equals(linhaGrid.get(k).findElement(By.tagName(tagName[3])).getAttribute("value"))) {
								achouTodos ++;
								((JavascriptExecutor) webDriver).executeScript(
										"arguments[0].scrollIntoView({block: \"start\", behavior: \"auto\"});", linhaGrid.get(k).findElement(By.tagName(tagName[3])));
								for (int p = 1; p <= check.length; p++) {
									linhaGrid.get(k+Integer.parseInt(check[p-1])).click();
									log.marcouCheck(valores.toString().trim());
									logManualUsuario.marcarCheck(valores.toString().trim());
								}
							}
						}
						if(valores.length == achouTodos) {
							achou = true;
							break;
						}
					}
				}
				if (achou)
					break;
				try {
					webDriver.findElement(By.xpath("//*[@id='formGeral:tableCamposProduto:dataFooterCamposProduto_table']/tbody/tr/td[5]"))
					.getAttribute("onclick").contains("Event.fire");
					proximaPagina = true;

				}catch (NullPointerException e) {
					proximaPagina = false;
				}
				if(proximaPagina){
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"start\", behavior: \"auto\"});", 
							webDriver.findElement(By.xpath("//*[@id='formGeral:tableCamposProduto:dataFooterCamposProduto_table']/tbody/tr/td[5]/img")));
					webDriver.findElement(By.xpath("//*[@id='formGeral:tableCamposProduto:dataFooterCamposProduto_table']/tbody/tr/td[5]/img")).click();
					delay(550);
				}
			}
			if (!achou) {
				throw new Exception("O registro -> " + valores + " <- não foi encontrado na lista.");
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void checkGridSemPaginacao() throws Exception {
		((JavascriptExecutor) webDriver).executeScript(
				"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
		String[] tagName = GenericType.getNavegarHTML().split("-");
		String[] valores = GenericType.getValor().split(",");
		int achouTodos = 0;
		try {
			boolean achou = false;
			for (int j = 0; j < 20; j++) {
				List<WebElement> gridItens = webDriver.findElement(By.xpath(GenericType.getElementoObsoleto())).findElements(By.tagName(tagName[0]));
				for (WebElement itemGrid : gridItens) {
					if (achou)
						break;
					List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
					for (int k = Integer.parseInt(tagName[2])-1; k < (linhaGrid.size()-1); k++) {
						for (int r = 0; r < valores.length; r++) {
							((JavascriptExecutor) webDriver).executeScript(
									"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", linhaGrid.get(k+1).findElement(By.tagName("label")));
							if (valores[r].trim().equals(linhaGrid.get(k+1).findElement(By.tagName("label")).getText())) {
								achouTodos ++;
								linhaGrid.get(k).click();
								log.marcouCheck(linhaGrid.get(k+1).findElement(By.tagName("label")).getText().toString().trim());
								logManualUsuario.marcarCheck(linhaGrid.get(k+1).findElement(By.tagName("label")).getText().toString().trim());
							}
						}
						if(valores.length == achouTodos) {
							achou = true;
							break;
						}
					}
				}
				if (achou)
					break;
			}
			if (!achou) {
				throw new Exception("O registro -> " + valores + " <- não foi encontrado na lista.");
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void mensagem() {
		try {
			delay(500);
			VerifyApresentacao.assertEquals(msg.obterMsgMensagemNaoEsperada(GenericType.getValor()), GenericType.getValor(),
					GenericType.getElemento().getText().replace("×\n", ""));
			log.verificouMensagemEsperada(GenericType.getValor());
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(
					msg.obterMsgMapeamentoMensagemIncorreto("mensagem de validação"));
		}
	}

	public void popup() {
		String[] valores = GenericType.getValor().split("-");
		try {
			VerifyApresentacao.assertEquals(msg.obterMsgMensagemNaoEsperada(valores[0]), valores[0],
					fecharAlertaRecuperarTexto(valores[1]));
			log.verificouMensagemEsperada(valores[0]);
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(
					msg.obterMsgMapeamentoMensagemIncorreto("mensagem de validação"));
		}
	}

	public void inserirPlan() throws Exception {
		try {
			String[] valoresNaoTratado = GenericType.getValor().split(",");
			String coluna = null;
			boolean proximo = false;
			int cont = 0;
			String fim;
			List<String> linhas = new ArrayList<String>();
			//pathRTA(getPathRTA1());
			for (String valorDesmenbrado : valoresNaoTratado) {
				fim = valorDesmenbrado;
				if(valoresNaoTratado.length-2 > cont) {
					cont = cont+1;
				}
				if(valorDesmenbrado.toUpperCase().contains("COLUNA") && !proximo) {
					coluna = valorDesmenbrado.substring(valorDesmenbrado.indexOf("_") + 1);
				} else if(!valorDesmenbrado.toUpperCase().contains("COLUNA")) {
					linhas.add(valorDesmenbrado);
					if(valoresNaoTratado[cont].toUpperCase().contains("COLUNA")	|| 
							valorDesmenbrado == valoresNaoTratado[valoresNaoTratado.length-1]) {
						proximo = true;
					}
				}
				if(proximo) {
					for (String linha : linhas) {
						String valorCampo;
						try {
							if (GenericType.getElemento().getAttribute("value").replace("×\n", "").equals("")) {
								valorCampo = GenericType.getElemento().getText().replace("×\n", "");
							} else {
								valorCampo = GenericType.getElemento().getAttribute("value").replace("×\n", "");
							}
						} catch (java.lang.NullPointerException e) {
							valorCampo = GenericType.getElemento().getText().replace("×\n", "");
						}
						xls.inserirPlan(linha, coluna, valorCampo);
					}
					proximo = false;
					coluna = null;
					linhas = new ArrayList<String>();
				}
			}
			//pathRTA(getPathRTA2());
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(
					msg.obterMsgMapeamentoMensagemIncorreto("mensagem de validação"));
		}
	}

	public void clicarGrid() throws Exception {
		((JavascriptExecutor) webDriver).executeScript(
				"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
		String[] tagName = GenericType.getNavegarHTML().split("-");
		try {
			boolean achou = false;
			for (int j = 0; j < 20; j++) {
				List<WebElement> gridItens = GenericType.getElemento().findElements(By.tagName(tagName[0]));
				for (WebElement itemGrid : gridItens) {
					if (achou)
						break;
					List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
					for (int k = 0; k < (linhaGrid.size()); k++) {
						if (tagName.length > 2) {
							linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
						}
						if (GenericType.getValor().equals(linhaGrid.get(k).getText())) {
							achou = true;
							linhaGrid.get(k).click();
							break;
						}
					}
				}
				if (achou)
					break;
			}
			if (!achou) {
				throw new Exception("O registro -> " + GenericType.getValor() + " <- não foi encontrado na lista.");
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void verificarItemGrid() throws Exception {
		((JavascriptExecutor) webDriver).executeScript(
				"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
		String[] tagName = GenericType.getNavegarHTML().split("-");
		try {
			boolean achou = false;
			for (int j = 0; j < 20; j++) {
				List<WebElement> gridItens = GenericType.getElemento().findElements(By.tagName(tagName[0]));
				for (WebElement itemGrid : gridItens) {
					if (achou)
						break;
					List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
					for (int k = 0; k < (linhaGrid.size()); k++) {
						if (tagName.length > 2) {
							linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
						}
						if (GenericType.getValor().equals(linhaGrid.get(k).getText())) {
							achou = true;
							break;
						}
					}
				}
				if (achou)
					break;
			}
			if (!achou) {
				throw new Exception("O registro -> " + GenericType.getValor() + " <- não foi encontrado na lista.");
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void clickItemGrid() throws Exception {
		aguardarElementoEstarPresente(GenericType.getElemento(), 5);
		((JavascriptExecutor) webDriver).executeScript(
				"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
		String[] tagName = GenericType.getNavegarHTML().split("-");
		try {
			boolean achou = false;
			for (int j = 0; j < 20; j++) {
				List<WebElement> gridItens = GenericType.getElemento().findElements(By.tagName(tagName[0]));
				for (WebElement itemGrid : gridItens) {
					if (achou)
						break;
					List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
					for (int k = 0; k < (linhaGrid.size()); k++) {
						if (GenericType.getValor().equals(linhaGrid.get(k).getText())) {
							int acao = linhaGrid.get(Integer.parseInt(tagName[2])-1).findElements(By.tagName(tagName[3])).size();
							linhaGrid = linhaGrid.get(Integer.parseInt(tagName[2])-1).findElements(By.tagName(tagName[3]));
							for (int p = 0; p < acao; p++) {
								if(Integer.parseInt(GenericType.getValorEsperadoElemento())-1 == p) {
									achou = true;
									linhaGrid.get(p).click();
									delay(4000);
									break;
								}
							}
							if (achou)
								break;
						}
					}
				}
				if (achou)
					break;
			}
			if (!achou) {
				throw new Exception("O registro -> " + GenericType.getValor() + " <- não foi encontrado na lista.");
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void clickItemGridVinculacoes() throws Exception {
		aguardarElementoEstarPresente(GenericType.getElemento(), 5);
		((JavascriptExecutor) webDriver).executeScript(
				"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
		String[] tagName = GenericType.getNavegarHTML().split("-");
		try {
			boolean achou = false;
			for (int j = 0; j < 20; j++) {
				List<WebElement> gridItens = GenericType.getElemento().findElements(By.tagName(tagName[0]));
				for (WebElement itemGrid : gridItens) {
					if (achou)
						break;
					List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
					for (int k = 0; k < (linhaGrid.size()); k++) {
						if (GenericType.getValor().equals(linhaGrid.get(k).getText())) {
							int acao = linhaGrid.get(Integer.parseInt(tagName[2])).findElements(By.tagName(tagName[3])).size();
							linhaGrid = linhaGrid.get(Integer.parseInt(tagName[2])).findElements(By.tagName(tagName[3]));
							for (int p = 0; p < acao; p++) {
								if(Integer.parseInt(GenericType.getValorEsperadoElemento())-1 == p) {
									achou = true;
									linhaGrid.get(p).click();
									delay(1000);
									break;
								}
							}
						}
					}
				}
				if (achou)
					break;
			}
			if (!achou) {
				throw new Exception("O registro -> " + GenericType.getValor() + " <- não foi encontrado na lista.");
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void radio() throws Exception {
		((JavascriptExecutor) webDriver).executeScript(
				"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
		try {
			GenericType.getElemento().click();
			log.marcouCheck(GenericType.getValorRTA());
			logManualUsuario.marcarCheck(GenericType.getValorRTA());

		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoCheckIncorreto(
					GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", "")));
		}
	}

	public void radioGrid() throws Exception {
		((JavascriptExecutor) webDriver).executeScript(
				"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", GenericType.getElemento());
		String[] tagName = GenericType.getNavegarHTML().split("-");
		int posicaoRadio;
		if (GenericType.getValorEsperadoElemento().equals("")) {
			posicaoRadio = 1;
		} else {
			posicaoRadio = Integer.parseInt(GenericType.getValorEsperadoElemento());
		}
		try {
			boolean achou = false;
			for (int h = 0; h < 20; h++) {
				List<WebElement> gridItens = GenericType.getElemento().findElements(By.tagName(tagName[0]));
				for (WebElement itemGrid : gridItens) {
					if (achou)
						break;
					List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
					for (int k = Integer.parseInt(tagName[2]); k < (linhaGrid.size()); k++) {
						if (GenericType.getValor().equals(linhaGrid.get(k).getText())) {
							achou = true;
							if (tagName.length > 3) {
								linhaGrid.get(k - posicaoRadio).findElement(By.tagName(tagName[3])).click();
							} else
								linhaGrid.get(k - posicaoRadio).click();
							log.selecionouRegistro(GenericType.getValor().toString());
							logManualUsuario.selecionarRegistro(GenericType.getValor().toString());
							break;
						}
					}
				}
				if (achou)
					break;
			}
			if (!achou) {
				throw new Exception("Valor -> " + GenericType.getValor() + " <- não encontrado.");
			}
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public static String getCombo(WebElement elemento) {
		Select combo = new Select(elemento);
		return combo.getAllSelectedOptions().get(0).getText();
	}

	public void path() {
		LerXls.setPath(MassaDadosUtil.getValueAsString(GenericType.getValor()));
	}

	public void mudarFrame(WebElement frame) {
		webDriver.switchTo().frame(frame);
	}

	public void mudarFrameDefault() {
		webDriver.switchTo().defaultContent();
	}

	public String recuperarCasoTeste() {
		String casoTeste = getCasoDeTesteAtual().getDeclaringClass().getSimpleName().toString();
		return casoTeste.replace("_", " ");
	}

	public static String data(int dia, int mes, int ano) {

		GregorianCalendar cl = new GregorianCalendar();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date data = new Date();
		;
		cl.setTime(data);

		// Pega a dia
		int dateAtual = cl.get(GregorianCalendar.DATE) + dia;
		cl.set(GregorianCalendar.DATE, dateAtual);
		Date dataAtual = cl.getTime();

		// Pega o Mês
		int month = cl.get(GregorianCalendar.MONTH) + mes;
		cl.set(GregorianCalendar.MONTH, mes);

		// Pega o ano
		int year = cl.get(GregorianCalendar.YEAR) + ano;
		cl.set(GregorianCalendar.YEAR, ano);

		return dateFormat.format(dataAtual);
	}

	public void coletarEvidencia() throws Exception {
		if (habilitarColetaEvidenciasTeste.equals("documento")) {
			coletarEvidenciaTeste();
		}
		if (habilitarColetaEvidenciasTeste.equals("print")) {
			coletarPrint();
		}
		if (ParametroUtil.getValueAsBoolean("coletaPrintManualUsuario")) {
			coletarPrintManualUsuario();
			TxtUtil.LerLogManualUsuario();
		}
	}

	private void coletarPrintManualUsuario() throws IOException {
		if(ParametroUtil.getValueAsBoolean("coletaPrintManualUsuario")){
			File scrFile = ((TakesScreenshot) GenericWebDriverSingleton.getDriver())
					.getScreenshotAs(OutputType.FILE);
			StringBuffer caminhoCompleto = new StringBuffer();
			caminhoCompleto.append(ParametroUtil
					.getValueAsString("pastaEvidencias"));
			caminhoCompleto
			.append(AbstractCenario.getCasoDeTesteAtual().getDeclaringClass().getPackage() + "//");
			caminhoCompleto.append(getDateTime());
			caminhoCompleto.append(" ");
			caminhoCompleto
			.append(AbstractCenario.getCasoDeTesteAtual().getDeclaringClass().getSimpleName());
			caminhoCompleto.append(".png");
			TxtUtil.setCaminhoPrint(caminhoCompleto);
			FileUtils.copyFile(scrFile, new File(caminhoCompleto.toString()));
		}
	}

	private void coletarPrint() {
		if (!getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Suite")
				&& (getCasoDeTesteAtual().equals(getCasoDeTestePrincipal())
						|| getCasoDeTesteAtual().getDeclaringClass().getSimpleName().equals("CenarioRealizarLogin"))) {
			StringBuffer caminhoCompleto = new StringBuffer();
			caminhoCompleto.append(ParametroUtil.getValueAsString("pastaArquivosEvidencias"));
			caminhoCompleto.append(getCasoDeTesteAtual().getDeclaringClass().getPackage() + "//");
			File file = new File(caminhoCompleto.toString());
			file.mkdir();
			caminhoCompleto.append("[");
			caminhoCompleto.append(getCasoDeTesteAtual().getDeclaringClass().getSimpleName());
			caminhoCompleto.append("]");
			caminhoCompleto.append(" ");
			caminhoCompleto.append(getDateTime());
			caminhoCompleto.append(" ");
			// caminhoCompleto.append(descricao);
			caminhoCompleto.append(".png");
			try {
				BufferedImage image = new Robot()
						.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				ImageIO.write(image, "png", new File(caminhoCompleto.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (!getCasoDeTesteAtual().getDeclaringClass().getSimpleName().contains("Auxiliar")) {
			StringBuffer caminhoCompleto = new StringBuffer();
			caminhoCompleto.append(ParametroUtil.getValueAsString("pastaArquivosEvidencias"));
			caminhoCompleto.append(getCasoDeTesteAtual().getDeclaringClass().getPackage() + "//");
			File file = new File(caminhoCompleto.toString());
			file.mkdir();
			caminhoCompleto.append("[");
			caminhoCompleto.append(getCasoDeTesteAtual().getDeclaringClass().getSimpleName());
			caminhoCompleto.append("]");
			caminhoCompleto.append(" ");
			caminhoCompleto.append(getDateTime());
			caminhoCompleto.append(" ");
			// caminhoCompleto.append(descricao);
			caminhoCompleto.append(".png");
			try {
				BufferedImage image = new Robot()
						.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				ImageIO.write(image, "png", new File(caminhoCompleto.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void coletarEvidenciaTeste() {
		BufferedImage bufImage = null;
		if (!getCasoDeTesteAtual().getDeclaringClass().getSimpleName().contains("Auxiliar")) {
			try {
				bufImage = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			addImgEvidencias(bufImage);
		}
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh.mm.ssaaa");
		Date date = new Date();
		return dateFormat.format(date);
	}

	private int qtdePaginas(int qtdRegistros) {
		int qtdPage = 1;
		if (qtdRegistros > 10) {
			if ((qtdRegistros % 10) == 0) {
				qtdPage = qtdRegistros / 10;
			} else {
				qtdPage = (qtdRegistros / 10) + 1;
			}
		}
		return qtdPage;
	}

	private List<String> montarListaParaAlvo(String... alvo) {
		List<String> alvos = new ArrayList<String>();
		for (String item : alvo) {
			alvos.add(item);
		}
		return alvos;
	}

	private String recuperarDescricaoClasse() {
		return AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName();
	}

	public void informarCamposJavaScript(String campo, String path, String valor) {
		((JavascriptExecutor) webDriver)
		.executeScript("$(" + campo + ").data('" + path + "').editor.setValue('" + valor + "');");
	}

	private void atualizarRelogio() throws IOException {
		if (!getCasoDeTesteAtual().getDeclaringClass().getSimpleName().contains("Auxiliar")
				&& !getCasoDeTesteAtual().getDeclaringClass().getSimpleName().equals("Caso_Teste_001")) {
			Relogio.incremetarNumeroCasosTesteAtual();
			Relogio.executarRelogio();
		}
		delay(300);
		//coletarEvidencia();
		delay();
	}

	protected void mouseOverElement(WebElement element) {
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		((JavascriptExecutor) webDriver).executeScript(mouseOverScript, element);
	}

	public String getPathTratado(String casoTeste) {
		return casoTeste.split("\\.")[casoTeste.split("\\.").length - 2].substring(0, 1).toLowerCase()
				+ casoTeste.split("\\.")[casoTeste.split("\\.").length - 2].substring(1);
	}

	/**
	 * Método que clica nos componentes da paginação, de acordo com o valor passado.
	 * 
	 * @throws Exception 
	 * @since 22/03/2019
	 */
	public void clickPaginador(WebElement paginador, WebElement qtdTotalRegistros) throws Exception {
		try {

			List<WebElement> paginacao = paginador.findElements(By.tagName("span"));
			switch (GenericType.getValorRTA()) {
			case "Primeira":
				paginacao.get(0).click();
				delay(1500);
				log.clicouPaginacao(GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", ""));
				coletarEvidencia();
				break;

			case "Anterior":
				paginacao.get(1).click();
				delay(1500);
				log.clicouPaginacao(GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", ""));
				coletarEvidencia();
				break;

			case "Próxima":
				paginacao.get(paginacao.size() - 3).click();
				delay(1500);
				log.clicouPaginacao(GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", ""));
				coletarEvidencia();
				break;

			case "Última":
				paginacao.get(paginacao.size() - 1).click();
				delay(1500);
				log.clicouPaginacao(GenericType.getValorRTA(), xls.lerPlan().replace("Módulo: ", ""));
				coletarEvidencia();
				break;

			case "":
				throw new Exception("Elemento de paginação: " + GenericType.getValorRTA() + "não encontrado!");

			default:
				break;
			}

		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(msg.obterMsgMapeamentoPaginacaoIncorreto() + nsee.getMessage());
		}
	}

}
