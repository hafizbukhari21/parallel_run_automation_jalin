import java.util.Properties;

import Services.WorkflowServices;
import util.PropertiesUtil;

public class WorkflowATMController {

    public static void main(String[] args ){
        
        /*
         * EOD
         * Invoice
         * Recon
         * ATU_unmask
         * BRI_Outbound
         * 
         * env =args[0] 1 VIT | 2 Staging
         * workflow_instance ID  = args[1]
         */
        Integer env = Integer.parseInt(args[0]);
        String worfklow_instance = args[1];
        WorkflowServices.GenerateReportATM_Controller(worfklow_instance,env);
    }
}
