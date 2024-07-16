package Services;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import util.DatabaseUtil;
import util.LogUtil;
import util.PropertiesUtil;
import util.ScheduleTask;

public class WorkflowServices {
    private static Properties workflowProp = PropertiesUtil.getInstance().getworkflowProp();
    private static Logger log = LogUtil.getLogger(DatabaseUtil.class.getName());


    private static void workflowUpdate(String handler_id, String worklfow_instance, Integer env){
        try {
            DatabaseUtil.executeUpdate(workflowProp.getProperty("query_set_workflow")
            .replace("$handlerID", handler_id )
            .replace("$workflowInstance", worklfow_instance ), env);
            log.info("Berhasil Reset handler = "+handler_id);
            SendTelegram.sendMessage("Menjalankan handler :"+handler_id);
        } catch (Exception e) {
            log.error(e);
            SendTelegram.sendMessage("Gagal handler :"+handler_id);

        }
    }


    private static void checkWorkflow(String handler_id, String worklfow_instance, Integer env){
          ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

        Runnable task = new ScheduleTask(){
          public void run(){
            String message = "";
            String failedTotal_history = DatabaseUtil.selectData(workflowProp.getProperty("query_check_workflow_failed").replace("$handlerID", handler_id ).replace("$workflowInstance", worklfow_instance ), "total", env);
            String totalHandler_history = DatabaseUtil.selectData(workflowProp.getProperty("query_check_workflow").replace("$handlerID", handler_id ).replace("$workflowInstance", worklfow_instance ), "total", env); 
            String totalSuccessHandler_history = DatabaseUtil.selectData(workflowProp.getProperty("query_check_workflow_success").replace("$handlerID", handler_id ).replace("$workflowInstance", worklfow_instance ), "total", env); 

            // if(Integer.parseInt(failedTotal)>0) {
            //     message = "Worflow_handler ="+handler_id+" | workflow_instance ="+worklfow_instance+" Gagal";
            //     log.error(message);
            //     SendTelegram.sendMessage(message);
            //     scheduler.shutdownNow();
            // }
            // else if (Integer.parseInt(totalHandler)== Integer.parseInt(totalSuccessHandler)){
            //     message = "Worflow_handler ="+handler_id+" | workflow_instance ="+worklfow_instance+" Success";
            //     log.info(message);
            //     SendTelegram.sendMessage(message);
            //     scheduler.shutdownNow();
            // }
            // else log.info("Workflow handler = "+handler_id+ " Still Running");

            String totalinstance = DatabaseUtil.selectData(workflowProp.getProperty("query_check_restart_rerun_total").replace("$handlerID", handler_id ).replace("$workflowInstance", worklfow_instance ), "total", env);
            String totalInProgress = DatabaseUtil.selectData(workflowProp.getProperty("query_check_restart_rerun_progress").replace("$handlerID", handler_id ).replace("$workflowInstance", worklfow_instance ), "total", env);
            String totalFinished  = DatabaseUtil.selectData(workflowProp.getProperty("query_check_restart_rerun_finished").replace("$handlerID", handler_id ).replace("$workflowInstance", worklfow_instance ), "total", env);


            if(Integer.parseInt(totalFinished)< Integer.parseInt(totalinstance)) {
                message = "Worflow_handler ="+handler_id+" | workflow_instance ="+worklfow_instance+" In Progress";
                log.info(message);
                SendTelegram.sendMessage(message);
            }
            
            else {
                if (Integer.parseInt(totalHandler_history)== Integer.parseInt(totalSuccessHandler_history)){
                    message = "Worflow_handler ="+handler_id+" | workflow_instance ="+worklfow_instance+" Success";
                    log.info(message);
                    SendTelegram.sendMessage(message);
                    scheduler.shutdownNow();
                }
                else {
                    message = "Worflow_handler ="+handler_id+" | workflow_instance ="+worklfow_instance+" Gagal";
                    log.info(message);
                    SendTelegram.sendMessage(message);
                    scheduler.shutdownNow();
                }

            }

          }

          
        };
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }


    public static void GenerateReportATM_Controller(String workflowInstance,Integer env){
        String handlerId = workflowProp.getProperty("workflow_id_atmController");
        //String workflowInstance =  workflowProp.getProperty("worflow_instance_atmController");

        workflowUpdate(handlerId,workflowInstance, env);
        checkWorkflow(handlerId, workflowInstance, env);
    }
}
