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
     try {
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
       SendTelegram.sendMessage("Berhasil menghapus data di RT_clearing");
     } catch (Exception e) {
      SendTelegram.sendMessage("Terjadi kesalahan saat menghapus data di rt_clearing");

      // TODO: handle exception
     }

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
        SendTelegram.sendMessage("Total Source 1 Pushed to be reason_unprocessed: "+totalExcecuted+" Data");

      } catch (Exception e) {
        logger.info(e);
      }
      
    }


    public static void checkSourceOne(Integer env){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

        Runnable task = new ScheduleTask(){
          public void run(){
            String totRemain = DatabaseUtil.selectData(progProp.getProperty("source_one_select"), "totalData", env);
            logger.info("Total Source 1 Unprocessed:"+totRemain+" Remainings");
            SendTelegram.sendMessage("Total Source 1 Unprocessed:"+totRemain+" Remainings");

            if(totRemain.equals("0")) scheduler.shutdownNow();
          }

        };
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }

    public static void BackupSource2000(Integer env, Integer multidate, String date){

      String nextDate = date;
      String prevDate = date;
     if(multidate == 2){
      nextDate = DateConvertion.SetDate(date, true);
      prevDate = DateConvertion.SetDate(date, false);
     }

      
      DatabaseUtil.Backup2000(progProp.getProperty("select_source_2000_backup").replace("$date", date).replace("$prev_date",prevDate).replace("$next_date",nextDate), env, date);

    } 

    public static void deleteElastic(Integer env){
      String route_ej_elastic_vit = progProp.getProperty("route_ej_elastic_vit");
      String route_ej_elastic_staging = progProp.getProperty("route_ej_elastic_staging");
      String route_recon_elastic_vit = progProp.getProperty("route_recon_elastic_vit");
      String route_recon_elastic_staging = progProp.getProperty("route_recon_elastic_staging");
      String endpoint = "http://"+progProp.getProperty("url_elastic")+"/";

      
      if(env ==1){
        HTTPService.httpRun(endpoint+route_ej_elastic_vit);
        HTTPService.httpRun(endpoint+route_recon_elastic_vit);
      }
      else{
        HTTPService.httpRun(endpoint+route_ej_elastic_staging);
        HTTPService.httpRun(endpoint+route_recon_elastic_staging);
      }

    }


    public static void deleteCounterInvoice(Integer env){
      try {
        DatabaseUtil.deletedQuery(progProp.getProperty("deleteCounter_invoice"), "TIERED_FEE_PERSISTANT_COUNT rennaisance_prop", env);
        SendTelegram.sendMessage("Berhasil menghapus Conter invoice bulanan TIERED_FEE_PERSISTANT_COUNT");

      } catch (Exception e) {
        logger.info(e);
      }

    }


    


}
