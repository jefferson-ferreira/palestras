
/**
 *  
 */

package massadedados;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import util.DataUtil;
import util.ExcelUtil;

/**
 * Classe que realizar a leitura de dados de uma planilha na extensão Xls.
 * 
 * @since 28/12/2018
 */
public class LerXls{

	private ArrayList<String>lstVariaveis;
	private static ArrayList<String>lstNomeCT;
	private static String path;
	private static String casoTeste;
	static boolean pathNovo = false;
	static boolean pathNovo2 = false;
	DataUtil dataUtil = new DataUtil();
	ExcelUtil excelUtil = new ExcelUtil();
	
	public static void setPath(String pathAtual) {
		if(!pathAtual.equals(path)  ){
			pathNovo = true;
			pathNovo2 = true;
		}
		LerXls.path = pathAtual;
		ExcelUtil.setPath(pathAtual);
		System.out.println("PATH -> " + path);
	}

	public static String getPath() {
		return path;
	}
	
	public static int getLinhaCasoTeste() {
		return lstNomeCT.indexOf(casoTeste)-1;
	}

	public static void setCasoTeste(String casoTeste) {
		LerXls.casoTeste = casoTeste;
		ExcelUtil.setCasoTeste(casoTeste);
	}

	public String lerPlanilhaDadosElementos(String nomeColuna, int linha){
		if(path == null && StringUtils.isNotBlank(getPath())){
			LerXls.setPath(getPath());
		}
		int abaPlan = 1;
		int linhaBase = 6;
		File fp = new File(path);
		WorkbookSettings conf = new WorkbookSettings();
		conf.setSuppressWarnings(true);
		conf.setEncoding("ISO-8859-1"); 
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(fp, conf);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}   
		Sheet aba = wb.getSheet(abaPlan); 
		Cell cellValue;
		if(pathNovo || lstVariaveis == null){
			lstVariaveis = new ArrayList<String>();
			for (int i = 0; i < aba.getColumns(); i++) {
				String aux = aba.getCell(i, linhaBase).getContents().toString().trim();
				lstVariaveis.add(aux);

			}
			pathNovo =false;
		}
		int n = aba.findCell(nomeColuna).getColumn();
		cellValue = aba.getCell(n, linha);

		String cellval = cellValue.getContents().toString().trim();
		return cellval;

	}

	/**
	 * Método que se passa o nome da linha e o nome da coluna, 
	 * e o método retorna o valor.
	 * Obs.: O valor da coluna e da linha passados deverá estar na primeira linha
	 * e primeira coluna da Planilha.
	 * 
	 * Ex.: Lerplanilha("CT-013", "Descricao");
	 * 
	 * @param nomeCT  - Nome do Caso de Teste.
	 * @param nomeColuna - Nome da Coluna.
	 * @return String - Retorna o valor da célula.
	 * 
	 * @throws Exception 
	 */
	public String lerPlanilha(String nomeColuna) throws Exception {
		int abaPlan = 1;
		int linhaBase = 6;
		int colunaBase = 0;
		int qtdLinhasAceitaveis = 1000;
		try { 
			// InputStream is = ParametroUtil.class.getResourceAsStream(path);
            File fp = new File(path);

			WorkbookSettings conf = new WorkbookSettings();  
			conf.setEncoding("ISO-8859-1"); 
			Workbook wb = Workbook.getWorkbook(fp, conf);   
			Sheet aba = wb.getSheet(abaPlan); 
			Cell cellValue;
			if(aba.getRows() > qtdLinhasAceitaveis){
				throw new Exception("Quantidade de linhas na planilha maior que 1000!");
			}
				lstVariaveis = new ArrayList<String>();
				for (int i = 0; i < aba.getColumns(); i++) {
					lstVariaveis.add(aba.getCell(i, linhaBase).getContents().toString().trim());
				}
				lstNomeCT = new ArrayList<String>();
				for (int i = 0; i < aba.getRows(); i++) {
					lstNomeCT.add(aba.getCell(colunaBase, i).getContents().toString().trim());
				}
			cellValue = aba.getCell(lstVariaveis.indexOf(nomeColuna), lstNomeCT.indexOf(casoTeste));
			if (cellValue.getType().equals(CellType.DATE)){  
				return lerDataExcel(cellValue);
			}else{
				return cellValue.getContents().toString().trim();
			}
		}   
		catch(ArrayIndexOutOfBoundsException ioe) {   
			throw new Exception("casoTeste: -> " + casoTeste + 
					" <- Ou Coluna: -> " + nomeColuna + " <- não foram encontrados na Planilha!!!  " + ioe);  
		}
	}

	public ArrayList<String> lerPlanilhaElementos() throws Exception {
		int abaPlan = 1;
		int linhaBase = 6;
		int qtdLinhasAceitaveis = 1000;
		File fp = new File(path);
		WorkbookSettings conf = new WorkbookSettings();  
		conf.setEncoding("ISO-8859-1"); 
		Workbook wb = Workbook.getWorkbook(fp, conf);   
		Sheet aba = wb.getSheet(abaPlan); 
		if(aba.getRows() > qtdLinhasAceitaveis){
			throw new Exception("Quantidade de linhas na planilha maior que 1000!");
		}
		if(pathNovo2 || lstVariaveis == null){
			lstVariaveis = new ArrayList<String>();
			for (int i = 3; i < aba.getColumns(); i++) {
				String string = aba.getCell(i, linhaBase).getContents().toString().trim();
				if(StringUtils.isEmpty(string)){
					break;
				}

				lstVariaveis.add(string);
			}
			pathNovo2 =false;
			return lstVariaveis;
		}
		return lstNomeCT;
	}
	
	public int qtdPlanilhas() throws Exception {
		int abaPlan = 1;
		int linhaBase = 6;
		int qtdLinhasAceitaveis = 1000;
		int contPath = 1;
		File fp = new File(path);
		WorkbookSettings conf = new WorkbookSettings();  
		conf.setEncoding("ISO-8859-1"); 
		Workbook wb = Workbook.getWorkbook(fp, conf);   
		Sheet aba = wb.getSheet(abaPlan); 
		if(aba.getRows() > qtdLinhasAceitaveis){
			throw new Exception("Quantidade de linhas na planilha maior que 1000!");
		}
		lstVariaveis = new ArrayList<String>();
		for (int i = 3; i < aba.getColumns(); i++) {
			String string = aba.getCell(i, linhaBase).getContents().toString().trim();
			if(StringUtils.isEmpty(string)){
				break;
			}
			if(string.contains("Path")) {
				contPath = contPath++;
			}

		}
		return contPath;
	}

	public String lerPlanCasoUso() throws Exception {
		// InputStream is = ParametroUtil.class.getResourceAsStream(path);
		File fp = new File(path);
		int abaPlan = 1;
		int linhaBase = 3;
		int qtdLinhasAceitaveis = 1000;

			WorkbookSettings conf = new WorkbookSettings();  
			conf.setEncoding("ISO-8859-1"); 
			Workbook wb = Workbook.getWorkbook(fp, conf);   
			Sheet aba = wb.getSheet(abaPlan); 

			if(aba.getRows() > qtdLinhasAceitaveis){
				throw new Exception("Quantidade de linhas na planilha maior que 1000!");
			}
			for (int i = 0; i < aba.getColumns(); i++) {
				String casoUso = (aba.getCell(i, linhaBase).getContents().toString().trim());
				if(casoUso.contains("Caso de Uso / Requisito:")){
					return casoUso;
				}
			}
		return null;
	}

	/**
	 * Método que retorna uma lista com os valores de acordo com o número da linha.
	 * 
	 * @param lin - Linha 
	 * @return ArrayList<String> - Uma lista com os valores da linha passada.
	 * @throws Exception 
	 */
	public ArrayList<String> lerLinhaPlanilha(int lin) throws Exception {
		ArrayList<String> lista = new ArrayList<String>();
		// InputStream is = ParametroUtil.class.getResourceAsStream(path);    
		File fp = new File(path);
		try {  
			Workbook wb = Workbook.getWorkbook(fp);   
			Sheet aba = wb.getSheet(0); 
			Cell cellValue =  null;

			for (int i = 0; i < aba.getColumns(); i++) {
				cellValue = aba.getCell(i, lin-1);
				//Se a célula conter data, trata o valor e adiciona na lista.
				if (cellValue.getType().equals(CellType.DATE)){  
					lista.add(lerDataExcel(cellValue));
				}else{
					lista.add(cellValue.getContents().toString());
				}
			}
			return lista;
		}   
		catch(Exception ioe) {   
			ioe.printStackTrace();
		}
		return lista;      		
	}

	public String lerPlan() throws Exception {
		File fp = new File(path);  
		int abaPlan = 1;
		int linhaBase = 3;
		int qtdLinhasAceitaveis = 1000;

		try { 
			WorkbookSettings conf = new WorkbookSettings();  
			conf.setEncoding("ISO-8859-1"); 
			Workbook wb = Workbook.getWorkbook(fp, conf);   
			Sheet aba = wb.getSheet(abaPlan); 

			if(aba.getRows() > qtdLinhasAceitaveis){
				throw new Exception("Quantidade de linhas na planilha maior que 1000!");
			}
			for (int i = 0; i < aba.getColumns(); i++) {
				String parametro = (aba.getCell(i, linhaBase).getContents().toString().trim());
				if(parametro.contains("Módulo")){
					return parametro;
				}
			}
		}   
		catch(ArrayIndexOutOfBoundsException ioe) {   
			throw new Exception("A coluna não foi encontrada na Planilha!!!  " + ioe);  
		}
		return null;
	}

	/**********************************************************************
	 * inserirStringPlan(Param1, Param2, Param3): inclui
	 * dados em uma planilha. Onde: 
	 * 
	 * Param1 = Linha
	 * Param2 = Coluna
	 * Param3 = Conteúdo
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void inserirStringPlan(int lin, int col, String conteudo) {
		try {
			int abaPlan = 1;
			col = col -1;
			lin = lin -1;
			HSSFWorkbook plan = new HSSFWorkbook(new FileInputStream(path));
			HSSFSheet aba = plan.getSheetAt(abaPlan);
			HSSFRow linhaFolha = aba.getRow(lin);
			HSSFCell celula = linhaFolha.getCell((short) col);
			celula.setCellType(HSSFCell.CELL_TYPE_STRING);
			celula.setCellValue(conteudo);
//			URL resourceUrl = getClass().getResource(path);
//			File file = new File(resourceUrl.toURI());
//			OutputStream stream = new FileOutputStream(file);
			
            FileOutputStream stream = new FileOutputStream(path);
			plan.write(stream);
			stream.flush();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	@SuppressWarnings("deprecation")
	public void inserirPlan(String linha, String nomeColuna, String conteudo) throws Exception {
		int abaPlan = 1;
		int linhaBase = 6;
		int colunaBase = 0;
		int qtdLinhasAceitaveis = 1000;
		try { 
			// InputStream is = ParametroUtil.class.getResourceAsStream(path);
			File fp = new File(path);
			WorkbookSettings conf = new WorkbookSettings();  
			conf.setEncoding("ISO-8859-1"); 
			Workbook wb = Workbook.getWorkbook(fp, conf);   
			Sheet aba = wb.getSheet(abaPlan); 
			if(aba.getRows() > qtdLinhasAceitaveis){
				throw new Exception("Quantidade de linhas na planilha maior que 1000!");
			}
			if(pathNovo2 || lstVariaveis == null){
				lstVariaveis = new ArrayList<String>();
				for (int i = 0; i < aba.getColumns(); i++) {
					lstVariaveis.add(aba.getCell(i, linhaBase).getContents().toString().trim());
				}
				//pathNovo =false;
			}
			if(pathNovo2 || lstNomeCT == null){
				lstNomeCT = new ArrayList<String>();
				for (int i = 0; i < aba.getRows(); i++) {
					lstNomeCT.add(aba.getCell(colunaBase, i).getContents().toString().trim());
				}
				pathNovo2 =false;
			}
			int col = lstVariaveis.indexOf(nomeColuna);
			int lin = Integer.parseInt(linha.replace(" ", ""));
			HSSFWorkbook plan = new HSSFWorkbook(new FileInputStream(path));
			HSSFSheet aba1 = plan.getSheetAt(abaPlan);
			HSSFRow linhaFolha = aba1.getRow(lin-1);
			HSSFCell celula = linhaFolha.getCell((short) col);
			celula.setCellType(HSSFCell.CELL_TYPE_BLANK);
			celula.setCellValue(conteudo);
//			URL resourceUrl = getClass().getResource(path);
//			File file = new File(resourceUrl.toURI());
//			OutputStream stream = new FileOutputStream(file);
			
            FileOutputStream stream = new FileOutputStream(path);
			plan.write(stream);
			stream.flush();
			stream.close();
			
		}   
		catch(ArrayIndexOutOfBoundsException ioe) {   
			throw new Exception("casoTeste: -> " + casoTeste + 
					" <- Ou Coluna: -> " + nomeColuna + " <- não foram encontrados na Planilha!!!  " + ioe);  
		}
	}
	
	@SuppressWarnings("deprecation")
	public void inserirPlan2(String nomeColuna, String conteudo) throws Exception {
		int abaPlan = 1;
		int linhaBase = 6;
		int colunaBase = 0;
		int qtdLinhasAceitaveis = 1000;
		try { 
			// InputStream is = ParametroUtil.class.getResourceAsStream(path);
			File fp = new File(path);
			WorkbookSettings conf = new WorkbookSettings();  
			conf.setEncoding("ISO-8859-1"); 
			Workbook wb = Workbook.getWorkbook(fp, conf);   
			Sheet aba = wb.getSheet(abaPlan); 
			if(aba.getRows() > qtdLinhasAceitaveis){
				throw new Exception("Quantidade de linhas na planilha maior que 1000!");
			}
			if(pathNovo2 || lstVariaveis == null){
				lstVariaveis = new ArrayList<String>();
				for (int i = 0; i < aba.getColumns(); i++) {
					lstVariaveis.add(aba.getCell(i, linhaBase).getContents().toString().trim());
				}
				//pathNovo =false;
			}
			if(pathNovo2 || lstNomeCT == null){
				lstNomeCT = new ArrayList<String>();
				for (int i = 0; i < aba.getRows(); i++) {
					lstNomeCT.add(aba.getCell(colunaBase, i).getContents().toString().trim());
				}
				pathNovo2 =false;
			}
			int col = lstVariaveis.indexOf(nomeColuna);
			int lin = lstNomeCT.indexOf(casoTeste);
			HSSFWorkbook plan = new HSSFWorkbook(new FileInputStream(path));
			HSSFSheet aba1 = plan.getSheetAt(abaPlan);
			HSSFRow linhaFolha = aba1.getRow(lin);
			HSSFCell celula = linhaFolha.getCell((short) col);
			celula.setCellType(HSSFCell.CELL_TYPE_STRING);
			celula.setCellValue(conteudo);
//			URL resourceUrl = getClass().getResource(path);
//			File file = new File(resourceUrl.toURI());
//			OutputStream stream = new FileOutputStream(file);
			
            FileOutputStream stream = new FileOutputStream(path);
			plan.write(stream);
			stream.flush();
			stream.close();
			
		}   
		catch(ArrayIndexOutOfBoundsException ioe) {   
			throw new Exception("casoTeste: -> " + casoTeste + 
					" <- Ou Coluna: -> " + nomeColuna + " <- não foram encontrados na Planilha!!!  " + ioe);  
		}
	}
	
	@SuppressWarnings("deprecation")
	public void inserirPlan3(String linha, String nomeColuna, String conteudo) throws Exception {
		int abaPlan = 1;
		int linhaBase = 6;
		int colunaBase = 0;
		int qtdLinhasAceitaveis = 1000;
		try { 
			// InputStream is = ParametroUtil.class.getResourceAsStream(path);
			File fp = new File(path);
			WorkbookSettings conf = new WorkbookSettings();  
			conf.setEncoding("ISO-8859-1"); 
			Workbook wb = Workbook.getWorkbook(fp, conf);   
			Sheet aba = wb.getSheet(abaPlan); 
			if(aba.getRows() > qtdLinhasAceitaveis){
				throw new Exception("Quantidade de linhas na planilha maior que 1000!");
			}
			if(pathNovo2 || lstVariaveis == null){
				lstVariaveis = new ArrayList<String>();
				for (int i = 0; i < aba.getColumns(); i++) {
					lstVariaveis.add(aba.getCell(i, linhaBase).getContents().toString().trim());
				}
				//pathNovo =false;
			}
			if(pathNovo2 || lstNomeCT == null){
				lstNomeCT = new ArrayList<String>();
				for (int i = 0; i < aba.getRows(); i++) {
					lstNomeCT.add(aba.getCell(colunaBase, i).getContents().toString().trim());
				}
				pathNovo2 =false;
			}
			int col = lstVariaveis.indexOf(nomeColuna);
			int lin = Integer.parseInt(linha)-1;
			// HSSFWorkbook plan = new HSSFWorkbook(getClass().getResourceAsStream(path));
            HSSFWorkbook plan = new HSSFWorkbook(new FileInputStream(path));
			HSSFSheet aba1 = plan.getSheetAt(abaPlan);
			HSSFRow linhaFolha = aba1.getRow(lin);
			HSSFCell celula = linhaFolha.getCell((short) col);
			celula.setCellType(HSSFCell.CELL_TYPE_STRING);
			celula.setCellValue(conteudo);

//			URL resourceUrl = getClass().getResource(path);
//			File file = new File(resourceUrl.toURI());
//			OutputStream stream = new FileOutputStream(file);
			
			File yourFile = new File(path);
			yourFile.createNewFile();
			FileOutputStream oFile = new FileOutputStream(yourFile, false); 			
			
			
			plan.write(oFile);
			oFile.flush();
			oFile.close();
			
		}   
		catch(ArrayIndexOutOfBoundsException ioe) {   
			throw new Exception("casoTeste: -> " + casoTeste + 
					" <- Ou Coluna: -> " + nomeColuna + " <- não foram encontrados na Planilha!!!  " + ioe);  
		}
	}

	@SuppressWarnings("deprecation")
	public void inserirPlanVersao(String conteudo) throws Exception {
		int abaPlan = 1;
		int qtdLinhasAceitaveis = 1000;
		try { 
			File fp = new File(path);
			WorkbookSettings conf = new WorkbookSettings();  
			conf.setEncoding("ISO-8859-1"); 
			Workbook wb = Workbook.getWorkbook(fp, conf);   
			Sheet aba = wb.getSheet(abaPlan); 
			if(aba.getRows() > qtdLinhasAceitaveis){
				throw new Exception("Quantidade de linhas na planilha maior que 1000!");
			}
			int col = 0;
			int lin = 4;
			HSSFWorkbook plan = new HSSFWorkbook(new FileInputStream(path));
			HSSFSheet aba1 = plan.getSheetAt(abaPlan);
			HSSFRow linhaFolha = aba1.getRow(lin);
			HSSFCell celula = linhaFolha.getCell((short) col);
			celula.setCellType(HSSFCell.CELL_TYPE_STRING);
			celula.setCellValue(conteudo);
			FileOutputStream stream = new FileOutputStream(path);
			plan.write(stream);
			stream.flush();
			stream.close();

		}   
		catch(ArrayIndexOutOfBoundsException ioe) {   

		}
	}

	/**********************************************************************
	 * inserirStringPlan(Param1, Param2, Param3): inclui
	 * dados em uma planilha. Onde: 
	 * 
	 * Param1 = Linha
	 * Param2 = Coluna
	 * Param3 = Conteúdo
	 * 
	 */
	public void inserirIntPlan(int lin, int col, int conteudo) {
		try {
			int abaPlan = 1;
			col = col -0;
			lin = lin -1;
			HSSFWorkbook plan = new HSSFWorkbook(new FileInputStream(path));
			HSSFSheet aba = plan.getSheetAt(abaPlan);
			HSSFRow linhaFolha = aba.getRow(lin);
			HSSFCell celula = linhaFolha.getCell((short) col);
			celula.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			celula.setCellValue(conteudo);
//			URL resourceUrl = getClass().getResource(path);
//			File file = new File(resourceUrl.toURI());
//			OutputStream stream = new FileOutputStream(file);
			
            FileOutputStream stream = new FileOutputStream(path);
			plan.write(stream);
			stream.flush();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	/**
	 * @param lin
	 * @param col
	 * @return string
	 */
	public String lerLinhaColunaPlan(int lin, int col) {
		// InputStream is = ParametroUtil.class.getResourceAsStream(path);    
		File fp = new File(path);
		try {  
			Workbook wb = Workbook.getWorkbook(fp);   
			Sheet aba = wb.getSheet(0);
			Cell cellValue =  aba.getCell(col-1, lin-1);
			if (aba.getCell(col-1, lin-1).getType().equals(CellType.DATE)){  
				return lerDataExcel(cellValue);
			}
			return cellValue.getContents().toString();		
		}   
		catch(Exception ioe) {   
			ioe.printStackTrace();    
		}
		return null;      		
	}

	

	/**
	 * Método que rotorna uma String para quando o valor da célula é uma Data no Excel.
	 * 
	 * Ex.: lerDataExcel(cellValue);
	 * 
	 * @param cellValue
	 * @return String
	 * @throws Exception 
	 */
	private String lerDataExcel(Cell cellValue) throws Exception{
		DateCell dateCell = (DateCell) cellValue;
		Date data = dateCell.getDate();
		return dataUtil.formataData(data);
	}

	/**********************************************************************
	 * naox(parametro): Serve para verificar se o conteúdo da variável(parâmetro) 
	 * é igual a 'X'. Este artifício é usando para indicar que o campo não terá
	 * nenhuma ação. 
	 * 
	 * Exemplo:  if (naox(vgLink1)) {
	 * 
	 *           } 
	 */                                                     
	public boolean naox(String valString){
		if(valString.equalsIgnoreCase("x")){
			return false;
		}else
			return true;            
	}

}

