package com.imporoney.ruby.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.imporoney.ruby.application.MyApplication;
import com.imporoney.ruby.modules.ItemDetail;
import com.imporoney.ruby.utils.NestedScrollManager;
import com.imporoney.ruby.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p/>
     * // * @param param1 Parameter 1.
     * // * @param param2 Parameter 2.
     *
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailFragment newInstance(Integer id) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    Integer id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setFragment();
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
    }


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.scrollView)
    NestedScrollView scrollView;
    @Bind(R.id.detail_logo)
    ImageView detail_logo;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.relate_info)
    TextView relate_info;
    @Bind(R.id.detail_image)
    ImageView detail_image;
    @Bind(R.id.detail_text)
    TextView detail_text;

    NestedScrollManager manager = new NestedScrollManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.detail, container, false);
        ButterKnife.bind(this, v);
        setupToolBar();
        manager.attach(scrollView);
        manager.addView(getActivity().findViewById(R.id.fab), NestedScrollManager.Direction.DOWN);
        setManager();
        init(id);
        return v;
    }

    private void setupToolBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setToolBar();
        collapsingToolbar.setTitle(itemDetail.getName());
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
     /*   toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.activity.onBackPressed();
            }
        });*/
    }

    @OnClick(R.id.btn_relate_shop)
    public void shopRelate(){

    }

    @OnClick(R.id.btn_similar_shop)
    public void shopSimilar(){

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Toolbar uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mListener.onFragmentInteraction(toolbar);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private static String TAG = "volley";
    private ItemDetail itemDetail;

    private void init(int id) {
        String url = "http://115.159.127.13:3000/things/detail_list/jsonData=json?item_id=" + id;
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            itemDetail = new Gson().fromJson(response.getJSONObject(0).toString(), ItemDetail.class);
                            Log.d(TAG, itemDetail.toString());
                            Picasso.with(getActivity()).load(MyApplication.baseUrl+itemDetail.getHeadlogo().getUrl())
                                    .into(detail_logo);
                            phone.setText("联系方式：" + itemDetail.getPhone());
                            address.setText("位置：" + itemDetail.getAddress());
                            title.setText(itemDetail.getName());
                            relate_info.setText(itemDetail.getImage_explain());
                            detail_text.setText(itemDetail.getThing_details().get(0).getDescription());
                            Picasso.with(getActivity()).load(MyApplication.baseUrl+itemDetail.getThing_details().get(0).getImage().getUrl())
                                    .into(detail_image);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstnce().addToRequestQueue(jsonObjReq);

    }

}
