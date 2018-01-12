package lekt07_lister3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import dk.nordfalk.android.elementer.R;

public class EkspanderbarRecyclerview extends AppCompatActivity {

  String[] landeArray = {"Danmark", "Norge", "Sverige", "Island", "Færøerne", "Finland",
          "Frankrig", "Spanien", "Portugal", "Nepal", "Indien", "Kina", "Japan", "Thailand"};
  // Vi laver en arrayliste så vi kan fjerne/indsætte elementer
  List<String> lande = new ArrayList<>(Arrays.asList(landeArray));
  List<List<String>> byer = new ArrayList<>();
  {
    byer.add(Arrays.asList("København", "Århus", "Odense", "Aalborg", "Ballerup" ) );
    byer.add(Arrays.asList("Oslo", "Trondheim" ) );
    byer.add(Arrays.asList("Stockholm", "Malmø", "Lund" ) );
    byer.add(Arrays.asList("Reykjavík", "Kópavogur", "Hafnarfjörður", "Dalvík" ) );
    byer.add(Arrays.asList("Tórshavn", "Klaksvík", "Fuglafjørður" ) );
    byer.add(Arrays.asList("Helsinki", "Espoo", "Tampere", "Vantaa" ) );
    byer.add(Arrays.asList("Paris", "Lyon" ) );
    byer.add(Arrays.asList("Madrid", "Barcelona", "Sevilla" ) );
    byer.add(Arrays.asList("Lissabon", "Porto" ) );
    byer.add(Arrays.asList("Kathmandu", "Bhaktapur" ) );
    byer.add(Arrays.asList("Mumbai", "Delhi", "Bangalore" ) );
    byer.add(Arrays.asList("Shanghai", "Zhengzhou" ) );
    byer.add(Arrays.asList("Tokyo", "Osaka", "Hiroshima", "Kawasaki", "Yokohama" ) );
    byer.add(Arrays.asList("Bankok", "Sura Thani", "Phuket" ) );
  }
  HashSet<Integer> åbneLande = new HashSet<>(); // hvilke lande der lige nu er åbne


  RecyclerView recyclerView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    recyclerView = new RecyclerView(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);

    setContentView(recyclerView);
  }


  RecyclerView.Adapter adapter = new RecyclerView.Adapter<EkspanderbartListeelemViewholder>() {

    @Override
    public int getItemCount()  {
      return lande.size();
    }

    @Override
    public EkspanderbartListeelemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
      LinearLayout rodLayout = new LinearLayout(parent.getContext());
      rodLayout.setOrientation(LinearLayout.VERTICAL);
      EkspanderbartListeelemViewholder vh = new EkspanderbartListeelemViewholder(rodLayout);
      vh.rodLayout = rodLayout;
      vh.landeview = getLayoutInflater().inflate(R.layout.lekt04_listeelement, parent, false);
      vh.overskrift = vh.landeview.findViewById(R.id.listeelem_overskrift);
      vh.beskrivelse = vh.landeview.findViewById(R.id.listeelem_beskrivelse);
      vh.åbnLukBillede = vh.landeview.findViewById(R.id.listeelem_billede);
      vh.overskrift.setOnClickListener(vh);
      vh.beskrivelse.setOnClickListener(vh);
      vh.åbnLukBillede.setOnClickListener(vh);
//      vh.åbnLukBillede.setBackgroundResource(android.R.drawable.btn_default);
      vh.overskrift.setBackgroundResource(android.R.drawable.list_selector_background);
      vh.beskrivelse.setBackgroundResource(android.R.drawable.list_selector_background);
      vh.landeview.setBackgroundResource(android.R.drawable.list_selector_background);
      vh.rodLayout.addView(vh.landeview);
      return vh;
    }

    @Override
    public void onBindViewHolder(EkspanderbartListeelemViewholder vh, int position) {
      boolean åben = åbneLande.contains(position);
      vh.overskrift.setText(lande.get(position) +" åben="+åben);
      vh.beskrivelse.setText("Land nummer " + position + " på vh@"+Integer.toHexString(vh.hashCode()));

      if (!åben) {
        vh.åbnLukBillede.setImageResource(android.R.drawable.ic_input_add); // vis 'åbn' ikon
        for (View underview : vh.underviews) underview.setVisibility(View.GONE); // skjul underelementer
      } else {
        vh.åbnLukBillede.setImageResource(android.R.drawable.ic_delete); // vis 'luk' ikon

        List<String> byerILandet = byer.get(position);

        while (vh.underviews.size()<byerILandet.size()) { // sørg for at der er nok underviews
          TextView underView = new TextView(vh.rodLayout.getContext());
          //underView.setPadding(0, 20, 0, 20);
          underView.setBackgroundResource(android.R.drawable.list_selector_background);
          underView.setOnClickListener(vh);      // lad viewholderen håndtere evt klik
          underView.setId(vh.underviews.size()); // unik ID så vi senere kan se hvilket af underviewne der klikkes på
          vh.rodLayout.addView(underView);
          vh.underviews.add(underView);
        }

        for (int i=0; i<vh.underviews.size(); i++) { // sæt underviews til at vise det rigtige indhold
          TextView underView = vh.underviews.get(i);
          if (i<byerILandet.size()) {
            underView.setText(byerILandet.get(i));
            underView.setVisibility(View.VISIBLE);
          } else {
            underView.setVisibility(View.GONE);      // for underviewet skal ikke bruges
          }
        }
      }
    }
  };


  /**
   * En Viewholder husker forskellige views i et listeelement, sådan at søgninger i viewhierakiet
   * med findViewById() kun behøver at ske EN gang.
   * Se https://developer.android.com/training/material/lists-cards.html
   */
  class EkspanderbartListeelemViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    LinearLayout rodLayout;
    TextView overskrift;
    TextView beskrivelse;
    ImageView åbnLukBillede;
    View landeview;
    ArrayList<TextView> underviews = new ArrayList<>();

    public EkspanderbartListeelemViewholder(View itemView) {
      super(itemView);
    }

    @Override
    public void onClick(View v) {
      final int position = getAdapterPosition();

      if (v == åbnLukBillede) { // Klik på billede åbner/lukker for listen af byer i dette land
        boolean åben = åbneLande.contains(position);
        if (åben) åbneLande.remove(position); // luk
        else åbneLande.add(position); // åbn
        adapter.notifyItemChanged(position);
      }
      else if (v == overskrift || v == beskrivelse) {
        Toast.makeText(v.getContext(), "Klik på " + position, Toast.LENGTH_SHORT).show();
      } else {
        int id = v.getId();
        Toast.makeText(v.getContext(), "Klik på by nummer " + id + " i "+lande.get(position), Toast.LENGTH_SHORT).show();
      }
    }
  }
}
