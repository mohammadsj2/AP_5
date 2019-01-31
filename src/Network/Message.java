package Network;

import Network.Client.Client;

public class Message
{
    String text;
    Client client;
    // Message replied;
    String repliedText;
    Client repliedClient;

    public Message(Client client, String text, Message replied)
    {
        this.text = text;
        this.client = client;
        if (replied != null)
        {
            this.repliedText = replied.getText();
            this.repliedClient = replied.getClient();
        } else
        {
            repliedText = null;
            repliedClient = null;
        }
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public String getRepliedText()
    {
        return repliedText;
    }

    public void setRepliedText(String repliedText)
    {
        this.repliedText = repliedText;
    }

    public Client getRepliedClient()
    {
        return repliedClient;
    }

    public void setRepliedClient(Client repliedClient)
    {
        this.repliedClient = repliedClient;
    }
}
