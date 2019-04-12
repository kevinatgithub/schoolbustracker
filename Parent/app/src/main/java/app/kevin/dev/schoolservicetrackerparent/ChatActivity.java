package app.kevin.dev.schoolservicetrackerparent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import app.kevin.dev.schoolservicetrackerparent.adapters.MessageAdapter;
import app.kevin.dev.schoolservicetrackerparent.lib.Session;
import app.kevin.dev.schoolservicetrackerparent.models.AppMessage;

public class ChatActivity extends AppCompatActivity {
    ListView lvMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String strParent = Session.get(this,"parent",null);

        if(strParent == null){

        }

        loadMessages();
    }

    private void loadMessages() {
        lvMessages = findViewById(R.id.lvMessages);

        ArrayList<AppMessage> messages = new ArrayList<>();
        messages.add(new AppMessage(1,getResources().getString(R.string.lorem),"2019-04-07 5:30PM",true));
        messages.add(new AppMessage(1,getResources().getString(R.string.lorem),"2019-04-07 5:30PM",true));
        messages.add(new AppMessage(1,getResources().getString(R.string.lorem),"20  19-04-07 5:30PM",false));

        MessageAdapter messageAdapter = new MessageAdapter(this,messages);
        lvMessages.setAdapter(messageAdapter);
    }

    public void showParentDetails(View view){
        findViewById(R.id.cvParentDetails).setVisibility(View.VISIBLE);
    }
}
