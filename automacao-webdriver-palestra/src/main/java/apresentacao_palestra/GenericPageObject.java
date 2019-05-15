/**
 * 
 */
package apresentacao_palestra;

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

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import driver.AbstractCenario;
import massadedados.LerXls;
import parametrizacao.MassaDadosUtil;
import parametrizacao.ParametroUtil;
import report.Report;
import util.MensagemLog;
import util.MensagemUtil;
import util.Relogio;

/**
 * 
 * @since 28/12/2018
 */
public class GenericPageObject extends AbstractCenario {

	private MensagemLog log = new MensagemLog();
	private MensagemUtil msg = new MensagemUtil();
	private static String habilitarColetaEvidenciasTeste = ParametroUtil.getValueAsString("coletaEvidenciasTeste");
	private LerXls xls = new LerXls();
	static Report report = new Report();
	private String pathRTA1;
	private String pathRTA2;

	public void pathRTA(String rta) throws Exception {
		LerXls.setPath(MassaDadosUtil.getValueAsString(rta));
		/*
		 * if (!AbstractCenario.getCasoDeTesteAtual().getDeclaringClass().getPackage().
		 * getName().contains("instalacao")) { xls.inserirPlanVersao(
		 * webDriver.findElement(By.xpath("//*[@id='rodape']/span")).getText()); } else
		 * if (AbstractCenario.getCasoDeTesteAtual().getDeclaringClass().getPackage().
		 * getName() .contains("instalacao")) {
		 * xls.inserirPlanVersao(webDriver.findElement(By.xpath(
		 * "//*[@id='footer-inner']/div[1]/p/b")).getText()); }
		 */
	}

	protected List<String> getListaJanelas() {
		String[] janelas = WebDriverSingleton.getDriver().getWindowHandles().toArray(new String[0]);
		List<String> listaJanelas = new ArrayList<String>();
		for (String janela : janelas) {
			listaJanelas.add(janela);
		}
		return listaJanelas;
	}

	public void mudarFrame(WebElement frame) {
		webDriver.switchTo().frame(frame);
	}

	public void mudarFrameDefault() {
		webDriver.switchTo().defaultContent();
	}

	public String getPathTratado(String casoTeste) {
		return casoTeste.split("\\.")[casoTeste.split("\\.").length - 2].substring(0, 1).toLowerCase()
				+ casoTeste.split("\\.")[casoTeste.split("\\.").length - 2].substring(1);
	}

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
							coletarEvidencia();
							log.clicouMenu(menus[0]);

							itensMenu = menuPrincipal.findElements(By.xpath("ul/li"));

							for (WebElement itemMenu : itensMenu) {
								if(itemMenu.findElement(By.xpath("a/span")).getText().equals(menus[1])){
									if(menus[2].equals("x")){
										itemMenu.findElement(By.xpath("a/span")).click();
										coletarEvidencia();
										log.clicouSubMenu(menus[1]);
										atualizarRelogio();
										return;
									}else {
										mouseOverElement(itemMenu.findElement(By.xpath("a/span")));
										delay(200);
										coletarEvidencia();
										log.clicouSubMenu(menus[1]);

										subItensMenu = itemMenu.findElements(By.tagName("li")); 
										for (WebElement subItem : subItensMenu) {
											if(subItem.findElement(By.xpath("a/span")).getText().equals(menus[2])){
												subItem.findElement(By.xpath("a/span")).click();
												log.clicouItemSubMenu(menus[2]);
												delay(450);
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
		delay(200);
	}

	private void atualizarRelogio() {
		if (!getCasoDeTesteAtual().getDeclaringClass().getSimpleName().contains("Auxiliar")
				&& !getCasoDeTesteAtual().getDeclaringClass().getSimpleName().equals("Caso_Teste_001")) {
			Relogio.incremetarNumeroCasosTesteAtual();
			Relogio.executarRelogio();
		}
		delay(150);
		coletarEvidencia();
		delay();
	}

	protected void mouseOverElement(WebElement element) {
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		((JavascriptExecutor) webDriver).executeScript(mouseOverScript, element);
	}

	public String getElement2(String tipoLocalizacaoElemento, String localizacaoElemento) {
		if (tipoLocalizacaoElemento.equals("outros")) {
			return localizacaoElemento;
		}
		return null;
	}

	public WebElement getElement(String tipoLocalizacaoElemento, String localizacaoElemento) {
		if (tipoLocalizacaoElemento.equals("xpath")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.xpath(localizacaoElemento)), 3);
			return webDriver.findElement(By.xpath(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("id")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.id(localizacaoElemento)), 3);
			return webDriver.findElement(By.id(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("css")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.cssSelector(localizacaoElemento)), 3);
			return webDriver.findElement(By.cssSelector(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("class")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.className(localizacaoElemento)), 3);
			return webDriver.findElement(By.className(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("name")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.name(localizacaoElemento)), 3);
			return webDriver.findElement(By.name(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("linkText")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.linkText(localizacaoElemento)), 3);
			return webDriver.findElement(By.linkText(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("tagName")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.tagName(localizacaoElemento)), 3);
			return webDriver.findElement(By.tagName(localizacaoElemento));
		}
		return null;
	}

	@SuppressWarnings("unused")
	public void executar(final ArrayList<String> arrayList) throws Exception {
		List<String> valoresRTA = montarListaParaAlvo(arrayList);
		for (int i = 0; i < valoresRTA.size(); i++) {
			String valor = xls.lerPlanilha(valoresRTA.get(i));
			if (xls.naox(valor)) {
				try {
					if (AbstractCenario.getCasoDeTesteAtual().getDeclaringClass().getPackage().getName()
							.contains("instalacao")) {
						aguardarElementoNaoEstarPresente(
								webDriver.findElement(By.xpath("//*[@id='form_dialogo_mensagem:modalStatusDialog']")), 150);
					} else {
						aguardarElementoNaoEstarPresente(
								webDriver.findElement(By.xpath("//*[@id='form_dialogo_mensagem:modalStatusDialog']")), 150);
					}
				} catch (org.openqa.selenium.NoSuchElementException e) {
				} catch (UnhandledAlertException e) {
				}
				WebElement elemento = null;
				String elemento2 = null;
				String tipoElemento = xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 5);
				if (xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 7).equals("outros")) {
					elemento2 = getElement2(xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 7),
							xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 8));
				} else {
					elemento = getElement(xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 7),
							xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 8));
				}
				String navegarHTML = xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 9);
				String valorEsperadoElemento = xls.lerPlanilhaDadosElementos(valoresRTA.get(i),
						LerXls.getLinhaCasoTeste());
				if (tipoElemento.equals("frame")) {
					delay(150);
					if (elemento2 == null) {
						mudarFrame(elemento);
					} else {
						mudarFrameDefault();
					}
				} else if (tipoElemento.equals("acoes")) {
					delay(150);
					String[] valores = valor.split("_");
					try {
						for (String valorTratado : valores) {
							if (valorTratado.toLowerCase().equals("f5")) {
								elemento.sendKeys(Keys.F5);
								delay(1000);
							} else if (valorTratado.toLowerCase().equals("tab")) {
								elemento.sendKeys(Keys.TAB);
								delay(100);
							} else if (valorTratado.toLowerCase().equals("click")) {
								elemento.click();
								delay(100);
							} else if (valorTratado.toLowerCase().equals("end")) {
								elemento.sendKeys(Keys.END);
								delay(100);
							} else if (valorTratado.toLowerCase().equals("foco")) {
								((JavascriptExecutor) webDriver).executeScript(
										"window.focus();", elemento);
								delay(100);
							}

						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException();
					}
				} else if (tipoElemento.equals("geraTexto")) {
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					if(AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Suite") && !AbstractCenario.getPosCondicao() && 
							!AbstractCenario.getPreCondicao()) {
						VerifyPalestra.assertEquals(
								msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0], recuperarCasoTeste()),
								valorEsperadoElemento, elemento.getAttribute("value"));
						log.comparouCampo(valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", ""));
					}
					geraString(Integer.parseInt(valor));
					log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
				} else if (tipoElemento.equals("geraTexto ckeditor javaScript")) {
					delay(200);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});",
							webDriver.findElement(By.xpath(xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 9))));
					((JavascriptExecutor) webDriver)
					.executeScript("CKEDITOR.instances['" + elemento.getAttribute("id").split("_")[1]
							+ "'].setData('" + geraString(Integer.parseInt(valor)) + "')");
					log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
				} else if (tipoElemento.equals("geraNumero")) {
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					if(AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Suite") && !AbstractCenario.getPosCondicao() && 
							!AbstractCenario.getPreCondicao()) {
						VerifyPalestra.assertEquals(
								msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0],
										recuperarCasoTeste()),
								valorEsperadoElemento, elemento.getAttribute("value"));
						log.comparouCampo(valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", ""));
					}
					elemento.sendKeys(geraNumero(Integer.parseInt(valor)));
					log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], elemento.getAttribute("value"));
				}else if (tipoElemento.equals("text")) {
					delay(100);
					try {
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						if(AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Suite") && !AbstractCenario.getPosCondicao() && 
								!AbstractCenario.getPreCondicao()) {
							VerifyPalestra.assertEquals(
									msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0],
											recuperarCasoTeste()),
									valorEsperadoElemento, elemento.getAttribute("value"));
							log.comparouCampo(valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", ""));
						}
						if (!valor.equals("") && !elemento.getAttribute("value").equals(valor)) {
							elemento.clear();
							elemento.sendKeys(valor);
							log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("label")) {
					try {
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						VerifyPalestra.assertEquals(
								msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0],
										recuperarCasoTeste()),
								valor, elemento.getText());
						log.comparouCampo(valoresRTA.get(i).split("_")[0], valor);
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("upload")) {
					delay(150);
					try {
						if (!valor.equals("")) {
							aguardarElementoEstarPresente(elemento, 2);
							elemento.click();
							Pattern nome = new Pattern(ParametroUtil.getValueAsString("imagemNome"));
							Pattern abrir = new Pattern(ParametroUtil.getValueAsString("imagemAbrir"));

							Screen src = new Screen();
							src.setAutoWaitTimeout(30);
							delay(150);
							src.type(nome, valor);
							src.click(abrir);

							log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("button")) {
					if(!valor.contains("NaoSuite") || AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Suite")) {
						delay(200);
						try {
							for (String valor2 : valor.split(",")) {
								aguardarElementoEstarPresente(elemento, 1);
								((JavascriptExecutor) webDriver).executeScript(
										"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
								coletarEvidencia();
								delay(150);
								elemento.click();
								log.clicouBotaoComParametro(valoresRTA.get(i).split("_")[0],
										xls.lerPlan().replace("Módulo: ", ""));
								delay(200);
							}
						} catch (NoSuchElementException nsee) {
							throw new NoSuchElementException(msg.obterMsgMapeamentoBotaoIncorreto(
									valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
						}
					}
				} else if (tipoElemento.equals("buttonList")) {
					delay(200);
					String[] tagName = navegarHTML.split("-");
					try {
						for (String valor2 : valor.split(",")) {
							aguardarElementoEstarPresente(elemento, 1);
							((JavascriptExecutor) webDriver).executeScript(
									"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
							coletarEvidencia();
							delay(150);
							elemento.findElement(By.tagName(tagName[0])).click();
							delay(150);
							try {
								boolean achou = false;
								for (int j = 0; j < 20; j++) {
									List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[1]));
									for (WebElement itemGrid : gridItens) {
										if (achou)
											break;
										List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
										for (int k = 0; k < (linhaGrid.size()); k++) {
											if (tagName.length > 3) {
												linhaGrid = itemGrid.findElements(By.tagName(tagName[3]));
											}
											if (valor.equals(linhaGrid.get(k).getText())) {
												achou = true;
												linhaGrid.get(k).click();
												log.clicouBotaoComParametro(valoresRTA.get(i).split("_")[0],
														xls.lerPlan().replace("Módulo: ", ""));
												delay(150);
												break;
											}
										}
									}
									if (achou)
										break;
								}
								if (!achou) {
									throw new Exception("O registro -> " + valor + " <- não foi encontrado na lista.");
								}
							} catch (NoSuchElementException nsee) {
								throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
							} catch (Exception e) {
								throw new Exception(e.getMessage());
							}
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoBotaoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("data")) {
					delay(150);
					try {
						if (AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName()
								.contains("Suite") && valoresRTA.get(i).contains("Editar")) {
							elemento.clear();
						}
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						if (valorEsperadoElemento.equals("D")) {
							valorEsperadoElemento = data(0, 0, 0);
						} else if (valorEsperadoElemento.contains("D")) {
							valorEsperadoElemento = data(
									Integer.parseInt(valorEsperadoElemento.replace("D", "").replace(" ", "")), 0, 0);
						}
						if(AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Suite") && !AbstractCenario.getPosCondicao() && 
								!AbstractCenario.getPreCondicao()) {
							VerifyPalestra.assertEquals(
									msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0],
											recuperarCasoTeste()),
									valorEsperadoElemento, elemento.getAttribute("value"));
							log.comparouCampo(valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", ""));
						}
						if (!elemento.getAttribute("value").equals(valor)) {
							if (valor.equals("D")) {
								((JavascriptExecutor) webDriver).executeScript(
										"arguments[0].value=('"+ data(0, 0, 0) +"')", elemento);
								log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], data(0, 0, 0));
							} else if (valor.contains("D")) {
								((JavascriptExecutor) webDriver).executeScript(
										"arguments[0].value=('"+ data(Integer.parseInt(valor.replace("D", "").replace(" ", "")), 0, 0) +"')", elemento);
								log.informouCampoComParametro(valoresRTA.get(i).split("_")[0],
										data(Integer.parseInt(valor.replace("D", "").replace(" ", "")), 0, 0));
							} else {
								((JavascriptExecutor) webDriver).executeScript(
										"arguments[0].value=('"+ valor +"')", elemento);
								log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
							}
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("labelData")) {
					delay(150);
					try {
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						if (valorEsperadoElemento.equals("D")) {
							valorEsperadoElemento = data(0, 0, 0);
						} else if (valorEsperadoElemento.contains("D")) {
							valorEsperadoElemento = data(
									Integer.parseInt(valorEsperadoElemento.replace("D", "").replace(" ", "")), 0, 0);
						}
						if(AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Suite") && !AbstractCenario.getPosCondicao() && 
								!AbstractCenario.getPreCondicao()) {
							VerifyPalestra.assertEquals(
									msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0],
											recuperarCasoTeste()),
									valorEsperadoElemento, elemento.getText());
							log.comparouCampo(valoresRTA.get(i).split("_")[0], data(Integer.parseInt(valor.replace("D", "").replace(" ", "")), 0, 0));
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("click")) {
					delay(150);
					try {
						coletarEvidencia();
						elemento.click();
						delay(150);
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoBotaoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("combo")) {
					delay(150);
					try {
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						if(AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Suite") && !AbstractCenario.getPosCondicao() && 
								!AbstractCenario.getPreCondicao()) {
						VerifyPalestra.assertEquals(
								msg.obterMsgComparacaoCombo(valoresRTA.get(i).split("_")[0], recuperarCasoTeste()),
								valorEsperadoElemento, getCombo(elemento));
						log.comparouCombo(xls.lerPlan().replace("Módulo: ", ""), valoresRTA.get(i).split("_")[0]);
						}

						if (!valor.equals("") && !getCombo(elemento).equals(valor)) {
							Select combo = new Select(elemento);
							combo.selectByVisibleText(valor);
							log.selecionouCombo(valor, valoresRTA.get(i).split("_")[0]);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoComboIncorreto(
								xls.lerPlan().replace("Módulo: ", ""), valoresRTA.get(i).split("_")[0]));

					}
				} else if (tipoElemento.equals("check")) {
					delay(150);
					String situacaoCheck = null;
					try {
						if(elemento.findElement(By.tagName("input")).getAttribute("checked").equals("true")) {
							situacaoCheck = "marcado";
						}
					} catch (java.lang.NullPointerException e) {
						situacaoCheck = "desmarcado";
					}
					try {
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						if(AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Suite") && !AbstractCenario.getPosCondicao() && 
								!AbstractCenario.getPreCondicao()) {
							VerifyPalestra.assertEquals(
									msg.obterMsgComparacaoCheck(valoresRTA.get(i).split("_")[0], recuperarCasoTeste(), 
											xls.lerPlan().replace("Módulo: ", "")), valorEsperadoElemento.toLowerCase(), situacaoCheck);
							log.comparouCheck(xls.lerPlan().replace("Módulo: ", ""), valoresRTA.get(i).split("_")[0]);
						}
						if (valor.toLowerCase().equals("marcar") && situacaoCheck.equals("desmarcado")) {
							elemento.click();
							delay(150);
							log.marcouCheck(valoresRTA.get(i).split("_")[0]);
						} else if (valor.toLowerCase().equals("desmarcar") && situacaoCheck.equals("marcado")) {
							elemento.click();
							delay(150);
							log.desmarcouCheck(valoresRTA.get(i).split("_")[0]);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(
								msg.obterMsgMapeamentoCheckIncorretoParametro(valoresRTA.get(i).split("_")[0]));
					}
				} else if (tipoElemento.equals("checkGrid")) {
					delay(150);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					String[] valores = valor.split("_");
					try {
						boolean achou = false;
						for (int j = 0; j < 20; j++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = 0; k < (linhaGrid.size()); k++) {
									if (valores[0].trim().equals(linhaGrid.get(k).getText())) {
										achou = true;
										linhaGrid.get(Integer.parseInt(tagName[2])-1).click();
										log.marcouCheck(valores.toString().trim());
										delay(150);
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
				} else if (tipoElemento.equals("mensagem")) {
					try {
						delay(150);
						VerifyPalestra.assertEquals(msg.obterMsgMensagemNaoEsperada(valor), valor,
								elemento.getText().replace("×\n", ""));
						log.verificouMensagemEsperada(valor);
						delay(150);
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(
								msg.obterMsgMapeamentoMensagemIncorreto("mensagem de validação"));
					}
				} else if (tipoElemento.equals("popup")) {
					String[] valores = valor.split("-");
					try {
						delay(150);
						VerifyPalestra.assertEquals(msg.obterMsgMensagemNaoEsperada(valores[0]), valores[0],
								fecharAlertaRecuperarTexto(valores[1]));
						log.verificouMensagemEsperada(valores[0]);
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(
								msg.obterMsgMapeamentoMensagemIncorreto("mensagem de validação"));
					}
				} else if (tipoElemento.equals("inserirPlan")) {
					try {

						String[] valoresNaoTratado = valor.split(",");
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
										if (elemento.getAttribute("value").replace("×\n", "").equals("")) {
											valorCampo = elemento.getText().replace("×\n", "");
										} else {
											valorCampo = elemento.getAttribute("value").replace("×\n", "");
										}
									} catch (java.lang.NullPointerException e) {
										valorCampo = elemento.getText().replace("×\n", "");
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
				} /*else if (tipoElemento.equals("inserirPlan")) {
					try {

						String[] valoresNaoTratado = valor.split(",");
						String coluna = null;
						boolean proximo = false;
						int cont = 0;
						List<String> linhas = new ArrayList<String>();
						//pathRTA(getPathRTA1());
						for (String valorDesmenbrado : valoresNaoTratado) {
							if(valoresNaoTratado.length-2 > cont) {
								cont = cont+1;
							}
							if(valorDesmenbrado.toUpperCase().contains("COLUNA") && !proximo) {
								coluna = valorDesmenbrado.substring(valorDesmenbrado.indexOf("_") + 1);
							} else if(!valorDesmenbrado.toUpperCase().contains("COLUNA")) {
								linhas.add(valorDesmenbrado);
								if(valoresNaoTratado[cont].toUpperCase().contains("COLUNA")) {
									proximo = true;
								} else if(valoresNaoTratado[cont].toUpperCase().equals(valorDesmenbrado)) {
									proximo = true;
								}
							}
							if(proximo) {
								for (String linha : linhas) {
									String valorCampo;
									try {
										if (elemento.getAttribute("value").replace("×\n", "").equals("")) {
											valorCampo = elemento.getText().replace("×\n", "");
										} else {
											valorCampo = elemento.getAttribute("value").replace("×\n", "");
										}
									} catch (java.lang.NullPointerException e) {
										valorCampo = elemento.getText().replace("×\n", "");
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
				}*/ else if (tipoElemento.equals("clicarGrid")) {
					delay(150);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					try {
						boolean achou = false;
						for (int j = 0; j < 20; j++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = 0; k < (linhaGrid.size()); k++) {
									if (tagName.length > 2) {
										linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
									}
									if (valor.equals(linhaGrid.get(k).getText())) {
										achou = true;
										linhaGrid.get(k).click();
										delay(150);
										break;
									}
								}
							}
							if (achou)
								break;
						}
						if (!achou) {
							throw new Exception("O registro -> " + valor + " <- não foi encontrado na lista.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} else if (tipoElemento.equals("verificarItemGrid")) {
					delay(150);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					delay(150);
					try {
						boolean achou = false;
						for (int j = 0; j < 20; j++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = 0; k < (linhaGrid.size()); k++) {
									if (tagName.length > 2) {
										linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
									}
									if (valor.equals(linhaGrid.get(k).getText())) {
										achou = true;
										break;
									}
								}
							}
							if (achou)
								break;
						}
						if (!achou) {
							throw new Exception("O registro -> " + valor + " <- não foi encontrado na lista.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} else if (tipoElemento.equals("clickItemGrid")) {
					aguardarElementoEstarPresente(elemento, 5);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					delay(300);
					try {
						boolean achou = false;
						for (int j = 0; j < 20; j++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = 0; k < (linhaGrid.size()); k++) {
									if (valor.equals(linhaGrid.get(k).getText())) {
										int acao = linhaGrid.get(Integer.parseInt(tagName[2])).findElements(By.tagName("a")).size();
										linhaGrid = linhaGrid.get(Integer.parseInt(tagName[2])).findElements(By.tagName(tagName[3]));
										for (int p = 0; p < acao; p++) {
											if(Integer.parseInt(valorEsperadoElemento)-1 == p) {
												achou = true;
												linhaGrid.get(0).findElements(By.tagName(tagName[4])).get(p).click();
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
							throw new Exception("O registro -> " + valor + " <- não foi encontrado na lista.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} else if (tipoElemento.equals("radio")) {
					delay(150);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					try {
						elemento.click();
						log.marcouCheck(valoresRTA.get(i).split("_")[0]);

					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCheckIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("radioGrid")) {
					delay(150);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					int posicaoRadio;
					if (valorEsperadoElemento.equals("")) {
						posicaoRadio = 1;
					} else {
						posicaoRadio = Integer.parseInt(valorEsperadoElemento);
					}
					try {
						boolean achou = false;
						for (int h = 0; h < 20; h++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = Integer.parseInt(tagName[2]); k < (linhaGrid.size()); k++) {
									if (valor.equals(linhaGrid.get(k).getText())) {
										achou = true;
										if (tagName.length > 3) {
											linhaGrid.get(k - posicaoRadio).findElement(By.tagName(tagName[3])).click();
										} else
											linhaGrid.get(k - posicaoRadio).click();
										log.selecionouRegistro(valor.toString());
										break;
									}
								}
							}
							if (achou)
								break;
						}
						if (!achou) {
							throw new Exception("Valor -> " + valor + " <- não encontrado.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} else if(tipoElemento.equals("path")) {
					LerXls.setPath(MassaDadosUtil.getValueAsString(valor));
				}
			}
		}
	}

	public static String getCombo(WebElement elemento) {
		Select combo = new Select(elemento);
		return combo.getAllSelectedOptions().get(0).getText();
	}

	public void coletarEvidencia() {
		if (habilitarColetaEvidenciasTeste.equals("documento")) {
			coletarEvidenciaTeste();
		}
		if (habilitarColetaEvidenciasTeste.equals("print")) {
			coletarPrint();
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

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
	private List<String> montarListaParaAlvo(String... alvo) {
		List<String> alvos = new ArrayList<String>();
		for (String item : alvo) {
			alvos.add(item);
		}
		return alvos;
	}

	private List<String> montarListaParaAlvo(ArrayList<String> arrayList) {
		List<String> alvos = new ArrayList<String>();
		for (String item : arrayList) {
			alvos.add(item);
		}
		return alvos;
	}

	public void informarCamposJavaScript(String campo, String path, String valor) {
		((JavascriptExecutor) webDriver)
		.executeScript("$(" + campo + ").data('" + path + "').editor.setValue('" + valor + "');");
	}

	public String recuperarCasoTeste() {
		String casoTeste = getCasoDeTesteAtual().getDeclaringClass().getSimpleName().toString();
		return casoTeste.replace("_", " ");
	}

	public String getPathRTA1() {
		return pathRTA1;
	}

	public void setPathRTA1(String pathRTA) {
		this.pathRTA1 = pathRTA;
	}

	public String getPathRTA2() {
		return pathRTA2;
	}

	public void setPathRTA2(String pathRTA) {
		this.pathRTA2 = pathRTA;
	}
}
