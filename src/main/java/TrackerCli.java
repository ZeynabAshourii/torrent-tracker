import control.Tracker;
import model.File;
import model.Log;
import model.Peer;

import java.util.AbstractMap;
import java.util.List;
import java.util.Scanner;

public class TrackerCli {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please enter the ip and port");
        String ipPort = scanner.nextLine();
        int port = 0;
        for (int i = 0; i < ipPort.length(); i++){
            if(ipPort.charAt(i) == ':'){
                port = Integer.parseInt(ipPort.substring(i+1 , ipPort.length()));
            }
        }
        Tracker tracker = new Tracker(port);
        System.out.println("tracker created");
        while (tracker.isAlive()){
            String order = scanner.nextLine();
            if (order.equals("request-logs")){
                for (int i = 0; i < Log.allLogs.size(); i++){
                    Log log = Log.allLogs.get(i);
                    if (log.type.equals("get")){
                        System.out.println("peer=" + log.peer.getID()
                                + "wants get file-name = " + log.file.getName() + "successfully=" + log.successfully);
                        getSeeders(tracker.getSeeders(log.file.getName()));
                    }
                }
            }
            else if (order.equals("all-logs")){
                allLogs();
            } else if (order.length() >= 10 && order.startsWith("file_logs")) {
                String fileName = order.substring(10 , order.length()-1);
                File file =  tracker.queryFileName(fileName);
                fileLogs(file);
            } else {
                System.out.println("your order is not valid");
            }
        }
    }
    public static void getSeeders(List<AbstractMap.SimpleEntry<Peer, Integer>> seeders){
        System.out.println("Seeders :");
        for (int i = 0; i < seeders.size(); i++){
            System.out.println(seeders.get(i));
        }
    }
    public static void allLogs(){
        for (int i = 0; i < Log.allLogs.size(); i++){
            Log log = Log.allLogs.get(i);
            if (log.type.equals("share")){
                System.out.println("peer=" + log.peer.getID()
                        + "wants share file-name = " + log.file.getName() + "successfully=" + log.successfully);
            }
        }
    }
    public static void fileLogs(File file){
        if (file == null){
            System.out.println("This file does not exist");
        }else {
            for (int i = 0; i < file.logs.size(); i++){
                System.out.println(file.logs.get(i));
            }

        }
    }

}
