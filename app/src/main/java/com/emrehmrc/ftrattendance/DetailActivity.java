package com.emrehmrc.ftrattendance;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emrehmrc.ftrattendance.adapter.DetailRecyclerAdapter;
import com.emrehmrc.ftrattendance.fragment.AddDetail;
import com.emrehmrc.ftrattendance.helper.LinearLayoutManagerWithSmoothScroller;
import com.emrehmrc.ftrattendance.helper.Methodes;
import com.emrehmrc.ftrattendance.model.Detail;
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

public class DetailActivity extends AppCompatActivity implements DialogListener, View.OnClickListener {

    private static final String TAG = "DetailActivity";
    private static final float ALPHA_FULL = 2.0f;
    PrefManager prefManager;
    String memberId;
    int a, b, c;
    private ImageView imgCalendar;
    private ImageView imgSetting;
    private ArrayList<Detail> detailList;
    private RecyclerView recyclerView;
    private DetailRecyclerAdapter mAdapter;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabBack;
    private TextView toolbarTitle;
    private TextView txtMemberName;
    private TextView txtA;
    private TextView txtB;
    private TextView txtC;
    private SimpleDateFormat format1;
    private ProgressBar pbLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Methodes.setLangCalendar(getBaseContext());
        setContentView(R.layout.activity_detail);
        prefManager = new PrefManager(this);
        memberId = prefManager.MemberId();
        init();
        deleteSwipe();
        setClickListeners();
        fabAdd.setOnClickListener(this);

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(DetailActivity.this, R
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
                                filterByDate();


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting
                Toasty.warning(getApplicationContext(), "Henüz Oluşturlmadı", Toast.LENGTH_SHORT,
                        true).show();
            }
        });

    }

    private void filterByDate() {
        a = 0;
        b = 0;
        c = 0;
        mAdapter.getFilter().filter(toolbarTitle.getText().toString(), new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                final ArrayList<Detail> filtered = SingletonCurrentValues.getInstance()
                        .getDetails();
                for (int i = 0; i < filtered.size(); i++) {
                    if (filtered.get(i).getState() == 1) a++;
                    if (filtered.get(i).getState() == 2) b++;
                    if (filtered.get(i).getState() == 3) c++;
                }
                txtA.setText(getString(R.string.check_a) + "(" + a + ")");
                txtB.setText(getString(R.string.check_b) + "(" + b + ")");
                txtC.setText(getString(R.string.check_c) + "(" + c + ")");
            }
        });
    }


    private void deleteSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper
                .SimpleCallback(0, ItemTouchHelper.LEFT ) {

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
                        removeFromDatabase(position);
                        break;
                    case ItemTouchHelper.RIGHT:
                        //showAddDialog(); Info
                        mAdapter.notifyItemRangeChanged(position, detailList.size());
                        break;

                }


            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void removeFromDatabase(final int position) {

        Detail detail = detailList.get(position);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("states").child(memberId);
        ref.child(detail.getDate()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(getApplicationContext(), "Başarıla Silindi", Toast.LENGTH_SHORT,
                        true).show();
                // memberList.remove(position);
                //  mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, detailList.size());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), "Beklenmeyen Hata", Toast.LENGTH_SHORT,
                        true).show();

            }
        });

    }

    private void init() {
        Log.d(TAG, "init");
        imgCalendar = findViewById(R.id.imgLeft);
        imgSetting = findViewById(R.id.imgRight);
        recyclerView = findViewById(R.id.memberRecycler);
        fabAdd = findViewById(R.id.fabAdd);
        fabBack = findViewById(R.id.fabBack);
        toolbarTitle = findViewById(R.id.toolbar_title);
        txtMemberName = findViewById(R.id.txtMemberName);
        txtA = findViewById(R.id.txtA);
        txtB = findViewById(R.id.txtB);
        txtC = findViewById(R.id.txtC);
        detailList = new ArrayList<>();
        mAdapter = new DetailRecyclerAdapter(this, detailList);
        recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        format1 = new SimpleDateFormat("dd MMM EEEE");
        pbLoad = findViewById(R.id.pbLoad);
        getMemberData();
        setCurrentDate();
        txtMemberName.setText(SingletonCurrentValues.getInstance().getMember().getName());
        fabBack.setOnClickListener(this);


    }

    private void setCurrentDate() {
        Calendar cal = Calendar.getInstance();
        String formatted = format1.format(cal.getTime());
        toolbarTitle.setText(formatted);


    }

    private void getMemberData() {
        Log.d(TAG, "getMemberData");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("states").child(memberId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                detailList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Detail detail = postSnapshot.getValue(Detail.class);
                    detailList.add(detail);

                }
                mAdapter.notifyDataSetChanged();
                filterByDate();
                pbLoad.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showAddDetailDialog() {

        android.app.FragmentManager fm = getFragmentManager();
        AddDetail addDetail = AddDetail.newInstance(this, toolbarTitle.getText().toString());
        addDetail.show(fm, "fragment_add_detail");
    }

    @Override
    public void onClosed(String name) {

    }

    @Override
    public void onClosedDetail(Detail detail) {
        addMemberDetailToFirebase(detail);
    }

    private void addMemberDetailToFirebase(Detail detail) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("states");
        // String id = ref.push().getKey();
        String id = toolbarTitle.getText().toString();
        detail.setDate(toolbarTitle.getText().toString());
        ref.child(memberId).child(id).setValue(detail).addOnSuccessListener(new
                                                                                    OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            Toasty.success(getApplicationContext(), "Başarıyla Eklendi", Toast
                                                                                                    .LENGTH_SHORT).show();


                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), "Beklenmeyen Hata", Toast
                        .LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAdd:
                showAddDetailDialog();
                break;
            case R.id.fabBack:
               onBackPressed();
                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
