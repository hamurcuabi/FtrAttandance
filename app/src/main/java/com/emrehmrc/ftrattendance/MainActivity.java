package com.emrehmrc.ftrattendance;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emrehmrc.ftrattendance.adapter.MemberRecyclerAdapter;
import com.emrehmrc.ftrattendance.fragment.AddMember;
import com.emrehmrc.ftrattendance.helper.LinearLayoutManagerWithSmoothScroller;
import com.emrehmrc.ftrattendance.helper.Methodes;
import com.emrehmrc.ftrattendance.model.Detail;
import com.emrehmrc.ftrattendance.model.Member;
import com.emrehmrc.ftrattendance.utils.SingletonCurrentValues;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements DialogListener, SearchView
        .OnQueryTextListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final float ALPHA_FULL = 2.0f;
    private ImageView imgCalendar;
    private ImageView imgSetting;
    private ArrayList<Member> memberList;
    private RecyclerView recyclerView;
    private MemberRecyclerAdapter mAdapter;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabClose;
    private SearchView searchView;
    private PrefManager prefManager;
    private TextView toolbarTitle;
    private SimpleDateFormat format1;
    private ProgressBar pbLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Methodes.setLangCalendar(getBaseContext());
        setContentView(R.layout.activity_main);
        prefManager = new PrefManager(this);
        prefManager.setFirstTimeLaunch(false);
        init();
        deleteSwipe();
        setClickListeners();
        searchView.setOnQueryTextListener(this);
    }


    private void setClickListeners() {

        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                Integer mYear = c.get(Calendar.YEAR);
                final Integer mMonth = c.get(Calendar.MONTH);
                Integer mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, R
                        .style.DateTime,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" +
                                // year);
                                toolbarTitle.setText(dayOfMonth + " " + (monthOfYear + 1) + " "
                                        + year);
                                SimpleDateFormat sp = new SimpleDateFormat("dd MM yyyy");
                                Date date = null;
                                try {
                                    date = sp.parse(dayOfMonth + " " + (monthOfYear + 1) + " "
                                            + year);
                                    String formatted = format1.format(date.getTime());
                                    toolbarTitle.setText(formatted);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting
               prefManager.setFirstTimeLaunch(!prefManager.isFirstTimeLaunch());
               if(!prefManager.isFirstTimeLaunch()) Toasty.info(getApplicationContext(),
                       "Öğretici Ekran Devre Dışı Bırakıldı",
                       Toast.LENGTH_SHORT,
                       true).show();
               else Toasty.success(getApplicationContext(),
                       "Öğretici Ekran Devrede",
                       Toast.LENGTH_SHORT,
                       true).show();

            }
        });

    }

    private void deleteSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper
                .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //  Toast.makeText(MainActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    Bitmap icon;
                    Paint paint = new Paint();

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX < 0) {
                        paint.setColor(Color.parseColor("#ffffff"));

                        RectF background = new RectF(
                                (float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, paint);

                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF iconDest = new RectF(
                                (float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width,
                                (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, iconDest, paint);

                    }
                    if (dX > 0) {
                        paint.setColor(Color.parseColor("#ffffff"));

                        RectF background = new RectF(
                                (float) itemView.getLeft() - dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.info);
                        RectF iconDest = new RectF(
                                (float) itemView.getLeft() + width, (float) itemView
                                .getTop() + width,
                                (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom
                                () -
                                width);
                        c.drawBitmap(icon, null, iconDest, paint);


                    }


                } else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    final float alpha = ALPHA_FULL - Math.abs(dY) / (float) viewHolder.itemView.getHeight();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationY(dY);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Toast.makeText(MainActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                final int position = viewHolder.getAdapterPosition();
                switch (swipeDir) {
                    case ItemTouchHelper.LEFT:
                        isYes(position);


                        break;
                    case ItemTouchHelper.RIGHT:
                        //showAddDialog(); Info

                        prefManager.setMemberId(memberList.get(position).getId());
                        SingletonCurrentValues.getInstance().setMember(memberList.get(position));
                        Intent i = new Intent(MainActivity.this, DetailActivity.class);
                        startActivity(i);
                        mAdapter.notifyItemRangeChanged(position, memberList.size());
                        break;

                }


            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void isYes(final int position) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        removeFromDatabase(position);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mAdapter.notifyItemRangeChanged(position, memberList.size());
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Kullanıcıyı Silmek İstiyor musunuz?")
                .setPositiveButton("Evet", dialogClickListener)
                .setNegativeButton("Hayır", dialogClickListener).show();

    }

    private void removeFromDatabase(final int position) {

        Member member = memberList.get(position);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(member.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Başarıyla Silindi", Toast.LENGTH_SHORT)
                        .show();
                // memberList.remove(position);
                //  mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, memberList.size());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Beklenmeyen Hata!", Toast.LENGTH_SHORT)
                        .show();

            }
        });

    }

    private void init() {
        Log.d(TAG, "init");
        imgCalendar = findViewById(R.id.imgLeft);
        imgSetting = findViewById(R.id.imgRight);
        recyclerView = findViewById(R.id.memberRecycler);
        searchView = findViewById(R.id.txtSearch);
        fabAdd = findViewById(R.id.fabAdd);
        fabClose = findViewById(R.id.fabClose);
        toolbarTitle = findViewById(R.id.toolbar_title);
        memberList = new ArrayList<>();
        mAdapter = new MemberRecyclerAdapter(this, memberList);
        recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        format1 = new SimpleDateFormat("dd MMM EEEE");
        pbLoad=findViewById(R.id.pbLoad);
        prepareMemberData();
        setCurrentDate();
        fabClose.setOnClickListener(this);
        fabAdd.setOnClickListener(this);

    }

    private void setCurrentDate() {
        Calendar cal = Calendar.getInstance();
        String formatted = format1.format(cal.getTime());
        toolbarTitle.setText(formatted);


    }

    private void prepareMemberData(){
        Log.d(TAG, "prepareMemberData");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                memberList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Member member = postSnapshot.getValue(Member.class);
                    memberList.add(member);
                }
                mAdapter.notifyDataSetChanged();
                pbLoad.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showAddDialog() {

        android.app.FragmentManager fm = getFragmentManager();
        AddMember addMember = AddMember.newInstance(this);
        addMember.show(fm, "fragment_add_member");
    }

    @Override
    public void onClosed(String name) {
        addMemberToFirebase(name);
    }

    @Override
    public void onClosedDetail(Detail detail) {

    }

    private void addMemberToFirebase(String name) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        String id = ref.push().getKey();
        final Member m = new Member(name, id);
        ref.child(id).setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toasty.success(getApplicationContext(), "Başarıyla Eklendi", Toast.LENGTH_SHORT,
                        true).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), "Beklenmeyen Hata", Toast.LENGTH_SHORT,
                        true).show();
            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAdd:
                showAddDialog();
                break;
            case R.id.fabClose:
                onBackPressed();
                break;

        }
    }
}
