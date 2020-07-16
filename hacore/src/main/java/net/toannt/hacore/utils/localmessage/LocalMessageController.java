package net.toannt.hacore.utils.localmessage;


public class LocalMessageController {

    private static LocalMessageController instance = null;

    private LocalMessageController() {
    }

    public static LocalMessageController getInstance() {
        if (instance == null) {
            instance = new LocalMessageController();
        }

        return instance;
    }

    public void processLocalMessage(LocalMessage message) {
        LocalMessageEventProvider.getAllEventBus(LocalMessage.class).post(message);
    }
}
