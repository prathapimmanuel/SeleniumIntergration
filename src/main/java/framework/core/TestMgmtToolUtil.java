package framework.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;

import atu.alm.wrapper.ALMServiceWrapper;
import atu.alm.wrapper.ITestCase;
import atu.alm.wrapper.ITestCaseRun;
import atu.alm.wrapper.enums.StatusAs;
import atu.alm.wrapper.exceptions.ALMServiceException;

public class TestMgmtToolUtil extends TestNGBase {

	
	static Properties testMgmtToolPrpts;

   private void loadProperties() {
	    
	     PropLoader	objPropLoader = new PropLoader();
		try {
			testMgmtToolPrpts = objPropLoader.loadProperties(
					EnvParameters.TEST_ROOT_DIR + File.separator + EnvParameters.TEST_MANAGEMENT_TOOL + ".Properties");
		} catch (Exception e) {
			Reporter.log("Exception in loading test management tool properties::" + e.getMessage());
		}
	}

	/**
	 * This method updates/Sends data to update the result in test management
	 * tool with the default params -read from the properties &test class methods
	 */
   public   void updateExecResutinTestMgmtTool(StatusAs status,String testCaseName) {
		 try {
			 switch(EnvParameters.TEST_MANAGEMENT_TOOL)	{
			 
			 case "Testlink":
				updateResultInTestLink();
				break;
			 case "HPALM":	
				 setALMParameters();
				 updateResultInALM(status,testCaseName);  
				 break;
			 }
		 } catch (Exception e) {			
			e.printStackTrace();
		}
   }
	 
		 
	
	/**
	 * This method updates/Sends data to update the result in test management
	 * tool with the  params - defined from the test class
	 */

	public  void updateExecResutinTestMgmtToolWithParams(StatusAs status,String testcaseName){		
		 loadProperties();			
		 switch(EnvParameters.TEST_MANAGEMENT_TOOL)	{				 
				 case "Testlink":
					updateResultInTestLink(testcaseName);
					break;
				 case "ALM":					 
					 updateResultInALM(status,testcaseName);  
					 break;
				 default:
						Reporter.log("No poperties to load");
				 }
		 
		
	}

	private void updateResultInTestLink() {
		setTestLinkParams(testMgmtToolPrpts.getProperty("TOOL_URL"), testMgmtToolPrpts.getProperty("AUTOMATION_KEY"),
				testMgmtToolPrpts.getProperty("PROJECT_NAME"), testMgmtToolPrpts.getProperty("PLAN_NAME"),
				testMgmtToolPrpts.getProperty("BUILD_NAME"),null);

	}

	private  void setALMParameters() {		
		setALMParams(testMgmtToolPrpts.getProperty("TOOL_URL"),testMgmtToolPrpts.getProperty("PROJECT_NAME") ,testMgmtToolPrpts.getProperty("DLL_PATH"),testMgmtToolPrpts.getProperty("USER_NAME"),testMgmtToolPrpts.getProperty("PASSWORD"),
				testMgmtToolPrpts.getProperty("AUTH_KEY1"),testMgmtToolPrpts.getProperty("AUTH_KEY2"),testMgmtToolPrpts.getProperty("DIR_PATH"),testMgmtToolPrpts.getProperty("TEST_EXCEL"));
		
	}

	
	//This is the code to log defect in HM ALM
	
	 /* public void createDefect() throws ALMServiceException {
		  System.setProperty("jacob.dll.path", TEST_MANAGEMENT_TOOL_DLL_PATH);
			ALMServiceWrapper wrapper = new ALMServiceWrapper(TEST_MANAGEMENT_TOOL_URL);
          wrapper.connect(TEST_MANAGEMENT_TOOL_USERNAME,TEST_MANAGEMENT_TOOL_PWD,TEST_MANAGEMENT_TOOL_AUTHKEY1,TEST_MANAGEMENT_TOOL_AUTHKEY2);
          IDefect defect = wrapper.newDefect();
          defect.isReproducible(true);
          defect.setAssignedTo("admin");
          defect.setDetectedBy("admin"); 
          defect.setDescription("My Defect Description");
          defect.setDetectionDate("12/1/2013");
          defect.setPriority(DefectPriority.HIGH);
          defect.setSeverity(DefectSeverity.MEDIUM);
          defect.setStatus(DefectStatus.OPEN); 
          defect.setSummary("My Defect/Bug Summary");
          //System.out.println(defect.getDefectID());
          wrapper.newAttachment("D:\\Data.xlsx", "My Attachment Description",
                       defect); 
          defect.save();
          wrapper.close();

   }*/
		
	 /**
	    * This method Updates result in HP ALM with the run status based on TestCase Name
	    * @param status
	    * @param testCaseName 
	    */
		public  void updateResultInALM(StatusAs status,String testCaseName) {
			System.setProperty("jacob.dll.path", TEST_MANAGEMENT_TOOL_DLL_PATH);
			ALMServiceWrapper wrapper = new ALMServiceWrapper(TEST_MANAGEMENT_TOOL_URL);
			try {
				
				FileInputStream ios = new FileInputStream(EXCEL_TEST_DETAILS_PATH);
				XSSFWorkbook workbook = new XSSFWorkbook(ios);
				XSSFSheet sheet = workbook.getSheetAt(0); 
				ArrayList<String> listStrings1 = new ArrayList<String>();
				ArrayList<String> listStrings2 = new ArrayList<String>();
				ArrayList<String> listStrings3 = new ArrayList<String>(); 

				ReadFromExcel read = new ReadFromExcel();
				listStrings1 = read.extractExcelContentByColumnIndex(0,sheet); 
				listStrings2 = read.extractExcelContentByColumnIndex(1,sheet); 
				listStrings3 = read.extractExcelContentByColumnIndex(2,sheet); 
				ITestCase testcase = null;
				for(int i=0;i<=listStrings1.size()-1;i++){
					
				if(listStrings1.get(i).equalsIgnoreCase(testCaseName)){
				wrapper.connect(TEST_MANAGEMENT_TOOL_USERNAME,TEST_MANAGEMENT_TOOL_PWD,TEST_MANAGEMENT_TOOL_AUTHKEY1,TEST_MANAGEMENT_TOOL_AUTHKEY2);
				testcase = wrapper.updateResult(TEST_MANAGEMENT_TOOL_PROJ_NAME, listStrings1.get(i),
						Integer.parseInt(listStrings2.get(i).toString()),listStrings3.get(i) , status); 
				}  
				
				}
				//below 3 lines are code for attachment feature in QC.
				
				/*String fname = FunctionalLibrary.getLatestFilefromDir(TEST_MANAGEMENT_TOOL_DIR_PATH).getName();
				wrapper.newAttachment(TEST_MANAGEMENT_TOOL_DIR_PATH + fname + "",
						"TC 11" + status + "", testcase); */
				

				HashMap<Integer,String>stepNRes = new HashMap<Integer,String>();
				
				stepNRes = getQCStepDescription(stepNRes,testCaseName);
					ITestCaseRun loginRun = wrapper.createNewRun(testcase, "Run 1",
							status);
					wrapper.addStep(loginRun, "Step 1", status,
							stepNRes.get(0), stepNRes.get(0),  
							stepNRes.get(0));  
					wrapper.addStep(loginRun, "Step 2", status, 
							stepNRes.get(1), stepNRes.get(1), 
							stepNRes.get(1)); 
					wrapper.addStep(loginRun, "Step 3", status,
							stepNRes.get(2), stepNRes.get(2),
							stepNRes.get(2));
					wrapper.addStep(loginRun, "Step 4", status,
							stepNRes.get(3), stepNRes.get(3),
							stepNRes.get(3));
					wrapper.addStep(loginRun, "Step 5", status,
							stepNRes.get(4), stepNRes.get(4),
							stepNRes.get(4));
					wrapper.addStep(loginRun, "Step 6", status,
							stepNRes.get(5), stepNRes.get(5),
							stepNRes.get(5));
					wrapper.addStep(loginRun, "Step 7", status,
							stepNRes.get(6), stepNRes.get(6),
							stepNRes.get(6));
					if(stepNRes.get(7)!=null){
					wrapper.addStep(loginRun, "Step 8", status,
							stepNRes.get(7), stepNRes.get(7), 
							stepNRes.get(7));
					} 
					if(stepNRes.get(8)!=null){
						wrapper.addStep(loginRun, "Step 8", status,
								stepNRes.get(8), stepNRes.get(8),
								stepNRes.get(8));
					}
					if(stepNRes.get(9)!=null){
						wrapper.addStep(loginRun, "Step 9", status,
								stepNRes.get(9), stepNRes.get(9),
								stepNRes.get(9));
					}
					if(stepNRes.get(10)!=null){
						wrapper.addStep(loginRun, "Step 10", status,
								stepNRes.get(10), stepNRes.get(10),
								stepNRes.get(10));
					}  
			      wrapper.close(); 
			      stepNRes.clear();
			} catch (ALMServiceException e) {
				
				e.printStackTrace();
			}
			catch (FileNotFoundException e) {
			
				e.printStackTrace();
			}
			catch (IOException e) {
				
				e.printStackTrace();
			}

		}
		
		/**
		 * This method gets the Test steps description form the excel sheet and returns a map of step Description
		 * @param statusMap
		 * @param testCaseName
		 * @return
		 */
		public HashMap<Integer, String>  getQCStepDescription(HashMap<Integer, String> statusMap,String testCaseName){
			try{
			ReadFromExcel read = new ReadFromExcel();
			FileInputStream ios = new FileInputStream(
					"D:\\Data Backup\\testCase_Mapping.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(ios);
			XSSFSheet sheet = workbook.getSheetAt(1); 
			ArrayList<String> listStrings1 = new ArrayList<String>();
			ArrayList<String> listStrings2 = new ArrayList<String>();
			ArrayList<String> listStrings3 = new ArrayList<String>();
			//ArrayList<String> listStrings4 = new ArrayList<String>();
			
			
			listStrings1 = read.extractExcelContentByColumnIndex(0, sheet);
			listStrings2 = read.extractExcelContentByColumnIndex(1, sheet);
			listStrings3 = read.extractExcelContentByColumnIndex(2, sheet);
			//listStrings4 = read.extractExcelContentByColumnIndex(3, sheet); 
			int j=0;
			for(int i=1;i<listStrings1.size();i++){
				if(listStrings1.get(i).equalsIgnoreCase(testCaseName)){ 
					statusMap.put(j, listStrings3.get(i));
					j++; 
				  }		
				  
			} 
			System.out.println("hmap"+statusMap);
			
			
			}catch(IOException e){
				e.printStackTrace();
			}

			return statusMap;
		}

/**
 * Thie method takes the testcase name  as parameter to update the result
 * @param testCaseName
 */
	
    private void updateResultInTestLink(String testCaseName) {
		
    	setTestLinkParams(testMgmtToolPrpts.getProperty("TOOL_URL"), testMgmtToolPrpts.getProperty("AUTOMATION_KEY"),
				testMgmtToolPrpts.getProperty("PROJECT_NAME"), testMgmtToolPrpts.getProperty("PLAN_NAME"),
				testMgmtToolPrpts.getProperty("BUILD_NAME"),testCaseName);
	}
}
