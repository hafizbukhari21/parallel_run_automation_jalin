import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import org.apache.log4j.Logger;
import Services.HellperExec;
import Services.MainService;
import Services.SendTelegram;
import util.DatabaseUtil;
import util.DateConvertion;
import util.LogUtil;
import util.PropertiesUtil;
import util.ScheduleTask;


class Main {
  private static Properties dpProp = PropertiesUtil.getInstance().getDbProp();
	private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
    public static void main(String[] args ){
      

      /*
       * arg 0 = Date YYYYMMDD
       * arg 1 = ENV  1=VIT|2=Staging
       * arg 2 = MultipleDate 
       * 
       */

      HellperExec hExec = new HellperExec() ;

      //Integer env = Integer.parseInt(args[0]);
      String envS = args[1]; //1=VIT 2=Staging
      
      String date = args[0];
      int multipleDate = Integer.parseInt(args[2]); //1=NO 2=Yes
      int env = Integer.parseInt(envS) ;

      //Initial Dev
      MainService.initial();
      
      //Delete Some RT_Clearing
      MainService.clearingRTClearing(env);
      
      //Push Source 1 Bisa multiple Date
      MainService.pushSourceOne(date,env,multipleDate);

      //Check Source 1 Udah Naik atau belum
      MainService.checkSourceOne(env);

      //Run Helper Baru Singgle Date
      hExec.Run("2000",date, envS);

      
    }

    
}





/*
 * 1 Delete rt_clearing Transaction data, Processed_fee, Processed_que_batch, Process_que_Trx, recon_result, Recons_source_que, summary(Plan with filter),
 * 2 Push Source 2000 to Source_data_que (Helper)
 * 3 Delete Source 2000 once Done
 * 4 Push Source 1
 */