package Network;

import Network.Client.Client;

import java.util.ArrayList;
import java.util.Objects;

public class CommonGameRequest {
    Client firstClient, secondClient;
    int initialMoney;
    int goalMoney;
    ArrayList<String> goalEntities;

    public CommonGameRequest(Client firstClient, Client secondClient, int initialMoney, int goalMoney, ArrayList<String> goalEntities) {
        this.firstClient = firstClient;
        this.secondClient = secondClient;
        this.initialMoney = initialMoney;
        this.goalMoney = goalMoney;
        this.goalEntities = goalEntities;
    }

    public int getInitialMoney() {
        return initialMoney;
    }

    public Client getFirstClient() {
        return firstClient;
    }

    public Client getSecondClient() {
        return secondClient;
    }

    public int getGoalMoney() {
        return goalMoney;
    }

    public ArrayList<String> getGoalEntities() {
        return goalEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonGameRequest that = (CommonGameRequest) o;
        return (firstClient.equals(that.firstClient) && secondClient.equals(that.secondClient))||
                (firstClient.equals(that.secondClient) && secondClient.equals(that.firstClient));
    }
}
