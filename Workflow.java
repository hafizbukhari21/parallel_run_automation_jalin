import Services.MainService;

public class Workflow {
    public static void main(String[] args ){
        /*
         * 
         * args[0]=date
         * arg 1 = ENV  1=VIT|2=Staging
         * 
         */
        String date = "20240505";
        String envS = "2";
        Integer env = Integer.parseInt(envS);


        //Set Process Control Date
        MainService.setProsessControlDate(date, env);

    }
}
