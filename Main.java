import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import org.apache.log4j.Logger;
import Services.HellperExec;
import util.DatabaseUtil;
import util.DateConvertion;
import util.LogUtil;
import util.PropertiesUtil;
import util.ScheduleTask;


class Main {
  private static Properties dpProp = PropertiesUtil.getInstance().getDbProp();
	private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
	private static Logger logger = LogUtil.getLogger(DatabaseUtil.class.getName());
    public static void main(String[] args ){
      logger.info("Apps By: M. Hafiz Bukhari");
      logger.info("Jalin - ITQA");

      /*
       * arg 0 = Date YYYYMMDD
       * arg 1 = ENV  1=VIT|2=Staging
       * arg 2 = MultipleDate 
       * 
       */

      HellperExec hExec = new HellperExec() ;

      //Integer env = Integer.parseInt(args[0]);
      int env = 2;//1=VIT 2=Staging
      String date = "20240703";
      int multipleDate = 2; //1=NO 2=Yes

      //Delete Some RT_Clearing
      //clearingRTClearing(env);
      

      //Push Source 1 Bisa multiple Date
      //pushSourceOne(date,env,multipleDate);

  
      //Check Source 1 Udah Naik atau belum
      //checkSourceOne(env);

      //Run Helper Baru Singgle Date
      hExec.Run("2000",date,"2");

      
    }


    private static void checkSourceOne(Integer env){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

        Runnable task = new ScheduleTask(){
          public void run(){
            String totRemain = DatabaseUtil.selectData(progProp.getProperty("source_one_select"), "totalData", env);
            logger.info("Total Source 1 Unprocessed:"+totRemain+" Remainings");

            if(totRemain.equals("0")) scheduler.shutdownNow();
          }
          
        };
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }

    private static void pushSourceOne(String date, Integer env, Integer multipleDate){
        String prevDate = date;
        String nextDate = date;
      
      //Run before and afterr Date if argumentes return 2
      if(multipleDate == 2){
        prevDate = DateConvertion.SetDate(date, false);
        nextDate = DateConvertion.SetDate(date, true);
      }
      
      Integer totalExcecuted = 0;
      try {
        totalExcecuted = DatabaseUtil.executeUpdate(progProp.getProperty("push_source_one").replace("$date", date).replace("$prev_date",prevDate).replace("$next_date",nextDate), env);
        logger.info("Total Source 1 Updated reason_unprocessed: "+totalExcecuted+" Data");
      } catch (Exception e) {
        
      }
      
    }

    private static void clearingRTClearing(Integer env){
      //Delete Processed_fee
      DatabaseUtil.deletedQuery(progProp.getProperty("deleteAllRT").replace("$tableName", "processed_fee"),"processed_fee", env );

      //Delete transaction_data

      //Delete Recon_result

      //Delete Recon_source_que

      //Delete Summary

      //Delete process_que_batch_data

      //Delete process_que_transaction
    }

    
}





/*
 * 1 Delete rt_clearing Transaction data, Processed_fee, Processed_que_batch, Process_que_Trx, recon_result, Recons_source_que, summary(Plan with filter),
 * 2 Push Source 2000 to Source_data_que (Helper)
 * 3 Delete Source 2000 once Done
 * 4 Push Source 1
 */