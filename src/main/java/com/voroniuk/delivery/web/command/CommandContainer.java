package com.voroniuk.delivery.web.command;


import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

public class CommandContainer {

    private static final Logger LOG = Logger.getLogger(CommandContainer.class);

    private static Map<String, Command> commands = new TreeMap<>();

    static {
        commands.put("noCommand", new NoCommand());
        commands.put("main", new MainCommand());
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("register", new RegisterCommand());
        commands.put("changeLocale", new ChangeLocaleCommand());
        commands.put("account", new AccountCommand());
        commands.put("makeOrder", new MakeOrderCommand());
        commands.put("manager_account", new ManagerAccountCommand());
        commands.put("user_account", new UserAccountCommand());
        commands.put("pay", new PayCommand());
        commands.put("delete", new DeleteCommand());
        commands.put("bill", new MakeBillCommand());
        commands.put("report", new ReportCommand());
        commands.put("send", new SendCommand());
        commands.put("arrived", new ArrivedCommand());
        commands.put("give_out", new GiveOutCommand());

        LOG.debug("Command container was successfully initialized");
        LOG.trace("Number of commands --> " + commands.size());
    }



    public static Command get(String commandName){

        if(commandName == null){
            commandName="main";
        }

        if (!commands.containsKey(commandName)) {
            LOG.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }

        return commands.get(commandName);
    }

}
