import java.util.Properties;

import org.apache.log4j.Logger;

import Services.MainService;
import util.DatabaseUtil;
import util.DateConvertion;
import util.LogUtil;
import util.PropertiesUtil;

public class PreRun {   
	private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
	private static Logger logger = LogUtil.getLogger(DatabaseUtil.class.getName());
    public static void main(String[] args ){
        /*
         * args[0] = date
         * args[1] = env 1=VIT | 2=Staging
         * args[2] = multidate 1=NO | 2=Yes
         * args[3] = prosesID
         * 
         */
        logger.info("Apps By: M. Hafiz Bukhari");
        logger.info("Jalin - ITQA");
        String date= DateConvertion.DateInputReformated(args[0]);
        Integer env = Integer.parseInt(args[1]);
        Integer multidate = Integer.parseInt(args[2]);
        Integer prosesID = Integer.parseInt(args[3]);

        // String date= "20240709";
        // Integer env = 2;
        // Integer multidate = 2;
        // Integer prosesID = 2;
         
       
        switch (prosesID) {
            case 1:
                //Backup Source 2000
                MainService.BackupSource2000(env, multidate, date);
                break;
            case 2:
                MainService.deleteCounterInvoice(env);
                //Delete Counter Invoice
                break;
            case 3:
                MainService.deleteElastic(env);
                //Reset Elastic
                break;
            default:
                break;
        }
        

        



    }
}
