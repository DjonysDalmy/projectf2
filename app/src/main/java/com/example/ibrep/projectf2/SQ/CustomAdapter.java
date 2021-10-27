package com.example.ibrep.projectf2.SQ;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ibrep.projectf2.Email.SendMail;
import com.example.ibrep.projectf2.R;

import java.util.ArrayList;

import static com.example.ibrep.projectf2.SQ.MainActivity.adapter;

/**
 * Created by Oclemmy on 5/5/2016 for ProgrammingWizards Channel and http://www.Camposha.com.
 */
public class CustomAdapter extends BaseAdapter {


    Context c;
    ArrayList<Spacecraft> spacecrafts;
    LayoutInflater inflater;
    Spacecraft spacecraft;
    AlertDialog AD;


    public CustomAdapter(Context c, ArrayList<Spacecraft> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    @Override
    public int getCount() {
        return spacecrafts.size();
    }

    @Override
    public Object getItem(int position) {
        return spacecrafts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(inflater==null)
        {
            inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.model,parent,false);
        }

        //BIND DATA
        MyViewHolder holder=new MyViewHolder(convertView);
        holder.nameTxt.setText(spacecrafts.get(position).getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(c, spacecrafts.get(position).getName(), Toast.LENGTH_SHORT).show();
                spacecraft= (Spacecraft) getItem(position);
                AD();
            }
        });

        holder.setLongClickListener(new MyLongClickListener() {
            @Override
            public void onItemLongClick() {
                spacecraft= (Spacecraft) getItem(position);
            }
        });

        return convertView;
    }

    public void AD () {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        //define o titulo
//        builder.setTitle("");
        //define a mensagem

        String str = adapter.getSelectedItemName();
        String WH = str.substring(0, 3);
        switch (WH) {
            case "KMP":
                String Mes = str.substring(0, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1))+";";
                builder.setMessage(Mes);
                break;
            case "CCC":
            case "CRD":
//                SpannableString s = new SpannableString("This is my link.");
//                s.setSpan(new URLSpan("http://www.google.com"), 11, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                String Mes1 = str.substring(0, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1)) + ";";
                builder.setMessage(Mes1);
//                String notacrd = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1)+9, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1)) + ";";
//                LayoutInflater factory = LayoutInflater.from(CustomAdapter.this);
//                final View view = factory.inflate(R.layout.sample, null);
//                builder.setView(R.xml.sample).show();
                builder.setPositiveButton("Nota", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String str1 = adapter.getSelectedItemName();
                        String notacrd = str1.substring(str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1)+9, str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1));
                        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(notacrd) );
                        browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(browse);
                    }
                });
                break;
            case "CAB":
                String Mes2 = str.substring(0, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1)) + ";";
                builder.setMessage(Mes2);
                builder.setPositiveButton("Nota", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String str1 = adapter.getSelectedItemName();
                        String notacab = str1.substring(str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";") + 1) + 1) + 1) + 1) + 1)+9, str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1));
                        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(notacab) );
                        browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(browse);
                    }
                });
                builder.setNegativeButton("Hodômetro", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String str1 = adapter.getSelectedItemName();
                        String hodometrocab = str1.substring(str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1)+21, str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1));
                        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(hodometrocab.trim()) );
                        browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(browse);
                    }
                });
                break;
            case "RCC":
                String Mes3 = str.substring(0, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1)) + ";";
                builder.setMessage(Mes3);
                builder.setPositiveButton("Nota", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String str1 = adapter.getSelectedItemName();
                        String notarcc = str1.substring(str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1)+9, str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1));
                        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(notarcc) );
                        browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(browse);
                    }
                });
                if (str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) != str.lastIndexOf( ';' )) {
                    final String autorizacaorcc = str.substring(str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 16, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1));
                    builder.setNegativeButton("Autorização", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(autorizacaorcc) );
                            browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            c.startActivity(browse);
                        }
                    });
                }
                break;
            case "CRC":
                String Mes4 = str.substring(0, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1)) + ";";
                builder.setMessage(Mes4);
                builder.setPositiveButton("Cheques", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String str1 = adapter.getSelectedItemName();
                        String chequecrc = str1.substring(str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";") + 1) + 1) + 1) + 1) + 1)+11, str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";", str1.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1));
                        Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(chequecrc));
                        browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(browse);
                    }
                });
                break;
        }



        builder.setNeutralButton("Copiar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String str = adapter.getSelectedItemName();
                String WH = str.substring(0, 3);
                switch (WH) {
                    case "KMP":
                        str = str.substring(0, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1)) + ";";
                        break;
                    case "CCC":
                    case "CRD":
                        str = str.substring(0, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1)) + ";";
                        break;
                    case "CAB":
                        str = str.substring(0, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1)) + ";";
                        break;
                    case "RCC":
                        str = str.substring(0, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1) + 1)) + ";";
                        break;
                    case "CRC":
                        str = str.substring(0, str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";", str.indexOf(";") + 1) + 1) + 1) + 1) + 1)) + ";";
                        break;
                    default:
                        str = "???";
                        break;
                }
                ClipboardManager clipboard = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(str, str);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(c,"Copiado",Toast.LENGTH_SHORT).show();
            }
        });

        //cria o AlertDialog
        AD = builder.create();
        //Exibe
        AD.show();
    }

    //EXPOSE NAME AND ID
    public int getSelectedItemID()
    {
        return spacecraft.getId();
    }
    public String getSelectedItemName()
    {
        return spacecraft.getName();
    }
}








