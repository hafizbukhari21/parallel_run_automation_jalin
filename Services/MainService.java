package Services;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import util.DatabaseUtil;
import util.DateConvertion;
import util.LogUtil;
import util.PropertiesUtil;
import util.ScheduleTask;

public class MainService {
    private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
	private static Logger logger = LogUtil.getLogger(DatabaseUtil.class.getName());

    public static void initial(){
        logger.info("Apps By: M. Hafiz Bukhari");
        logger.info("Jalin - ITQA");
    }


    public static void setProsessControlDate(String date,Integer env){
        String newDate = DateConvertion.NewDateFormat(date);
        String newDatePrev = DateConvertion.NewDateFormatPrev(date) ;
        try {
            DatabaseUtil.executeUpdate(progProp.getProperty("update_process_control").replace("$date", newDate).replace("$prevDate", newDatePrev), env);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static void clearingRTClearing(Integer env){
      //Delete Processed_fee
      DatabaseUtil.deletedQuery(progProp.getProperty("deleteAllRT").replace("$tableName", "processed_fee"),"processed_fee", env );

      //Delete transaction_data
      DatabaseUtil.deletedQuery(progProp.getProperty("deleteAllRT").replace("$tableName", "transaction_data"),"transaction_data", env );

      //Delete Recon_result
      DatabaseUtil.deletedQuery(progProp.getProperty("deleteAllRT").replace("$tableName", "recon_results"),"recon_results", env );

      //Delete Recon_source_key
      DatabaseUtil.deletedQuery(progProp.getProperty("deleteAllRT").replace("$tableName", "recon_source_keys"),"recon_source_keys", env );

      //Delete process_que_batch_data
      DatabaseUtil.deletedQuery(progProp.getProperty("deleteAllRT").replace("$tableName", "process_queue_batched_trx"),"process_queue_batched_trx", env );

      //Delete process_que_transaction
      DatabaseUtil.deletedQuery(progProp.getProperty("deleteAllRT").replace("$tableName", "process_queue_trans_batch"),"process_queue_trans_batch", env );

    }


    public static void pushSourceOne(String date, Integer env, Integer multipleDate){
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


    public static void checkSourceOne(Integer env){
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


}
