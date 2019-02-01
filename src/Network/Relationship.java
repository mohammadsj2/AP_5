package Network;

import Network.Client.Client;

public class Relationship {
    Client firstClient,secondClient;
    boolean firstClientAccepted,secondClientAccepted;
    String requestMesage="";
    int numberOfCommonGame=0;

    public Relationship(Client firstClient, Client secondClient) {
        this.firstClient = firstClient;
        this.secondClient = secondClient;
    }
    public void clientAccept(Client client){
        if(firstClient.equals(client)){
            firstClientAccepted=true;
        }
        if(secondClient.equals(client)){
            secondClientAccepted=true;
        }
    }

    public void setNumberOfCommonGame(int numberOfCommonGame) {
        this.numberOfCommonGame = numberOfCommonGame;
    }
    public void plusOneNumberOfCommonGame(){
        numberOfCommonGame++;
    }

    public int getNumberOfCommonGame() {
        return numberOfCommonGame;
    }

    public void setRequestMesage(String requestMesage) {
        this.requestMesage = requestMesage;
    }

    public String getRequestMesage() {
        return requestMesage;
    }

    public boolean isFriend() {
        return firstClientAccepted && secondClientAccepted;
    }
    public boolean isUnFriend(){
        return !(firstClientAccepted || secondClientAccepted);
    }

    public boolean isClientAccepted(Client client) {
        if(client.equals(firstClient)){
            return firstClientAccepted;
        }
        return secondClientAccepted;
    }

    public Client getFirstClient() {
        return firstClient;
    }

    public Client getSecondClient() {
        return secondClient;
    }

    public void reject() {
        firstClientAccepted=secondClientAccepted=false;
    }
}
