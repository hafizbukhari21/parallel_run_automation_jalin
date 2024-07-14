import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import org.apache.log4j.Logger;
import Services.HellperExec;
import util.DatabaseUtil;
import util.LogUtil;
import util.PropertiesUtil;
import util.ScheduleTask;


class Main {
  private static Properties dpProp = PropertiesUtil.getInstance().getDbProp();
	private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
	private static Logger logger = LogUtil.getLogger(DatabaseUtil.class.getName());
    public static void main(String[] args ){
      HellperExec hExec = new HellperExec() ;

      //Integer env = Integer.parseInt(args[0]);
      int env = 1;//VIT
      String date = "20240614";

      //Push Source 1
      pushSourceOne(date,env);

  
      //Check Source 1 Udah Naik atau belum
      checkSourceOne(env,date);

      //Run Helper
     hExec.Run("2000",date,"1");

      
    }


    private static void checkSourceOne(Integer env, String date){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

        Runnable task = new ScheduleTask(){
          public void run(){
            String totRemain = DatabaseUtil.selectData(progProp.getProperty("source_one_select").replace("$date", date), "totalData", env);
            logger.info("Total Source 1 Unprocessed:"+totRemain+" Remainings");

            if(totRemain.equals("0")) scheduler.shutdownNow();
          }
          
        };
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }

    private static void pushSourceOne(String date, Integer env){
      Integer totalExcecuted = 0;
      try {
        totalExcecuted = DatabaseUtil.executeUpdate(progProp.getProperty("push_source_one").replace("$date", date), env);
        logger.info("Total Source 1 Updated reason_unprocessed: "+totalExcecuted+" Data");
      } catch (Exception e) {
        
      }
      
    }

    private static void clearingRTClearing(){
      //Delete Processed_fee
      //DatabaseUtil.deletedQuery(progProp.getProperty("deleteAllRT").replace("$tableName", "processed_fee"),"processed_fee", env );
    }

    
}





/*
 * 1 Delete rt_clearing Transaction data, Processed_fee, Processed_que_batch, Process_que_Trx, recon_result, Recons_source_que, summary(Plan with filter),
 * 2 Push Source 2000 to Source_data_que (Helper)
 * 3 Delete Source 2000 once Done
 * 4 Push Source 1
 */