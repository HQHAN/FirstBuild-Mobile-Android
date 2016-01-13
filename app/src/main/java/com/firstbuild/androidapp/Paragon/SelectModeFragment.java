package com.firstbuild.androidapp.paragon;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstbuild.androidapp.R;
import com.firstbuild.androidapp.paragon.dataModel.BuiltInRecipeFoodsInfo;
import com.firstbuild.androidapp.paragon.dataModel.BuiltInRecipeInfo;
import com.firstbuild.androidapp.paragon.dataModel.RecipeManager;
import com.firstbuild.androidapp.paragon.helper.SelectModeAdapter;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectModeFragment extends Fragment  implements SelectModeAdapter.ClickListener {


    private String TAG = SelectModeFragment.class.getSimpleName();
    private RecyclerView listMode;
    private SelectModeAdapter selectModeAdapter;
    private SelectModeSteps selectModeSteps;
    private View layoutButtons;
    private ParagonMainActivity attached;
    private BuiltInRecipeFoodsInfo builtRecipes = null;

    private enum SelectModeSteps {
        STEP_COOKING_METHOD,
        STEP_MATERIAL,
        STEP_HOW_TO_COOK,
    }


    public SelectModeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        attached = (ParagonMainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_mode, container, false);

        view.findViewById(R.id.btn_my_recipes).setVisibility(View.GONE);
        view.findViewById(R.id.btn_my_recipes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onclick my recipes");
                RecipeManager.getInstance().ReadFromFile();
                attached.nextStep(ParagonMainActivity.ParagonSteps.STEP_MY_RECIPES);

            }
        });

        view.findViewById(R.id.btn_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onclick Quick Start");

                RecipeManager.getInstance().createRecipeSousVide();
                attached.nextStep(ParagonMainActivity.ParagonSteps.STEP_QUICK_START);

            }
        });

        layoutButtons = view.findViewById(R.id.layout_buttons);

        listMode = (RecyclerView) view.findViewById(R.id.list_mode);

        selectModeAdapter = new SelectModeAdapter(getActivity());
        selectModeAdapter.setClickListener(this);

        SlideInUpAnimator animator = new SlideInUpAnimator();
        animator.setRemoveDuration(100);
        animator.setAddDuration(200);
        listMode.setItemAnimator(animator);
        listMode.setAdapter(selectModeAdapter);
        listMode.setLayoutManager(new LinearLayoutManager(getActivity()));

        builtRecipes = attached.builtInRecipes;

        selectModeSteps = SelectModeSteps.STEP_COOKING_METHOD;
        removeAllList();
        fillList();

        ((ParagonMainActivity)getActivity()).setTitle("Paragon");

        return view;
    }


    private void removeAllList(){
        int size = selectModeAdapter.getItemCount();

        for (int i = 0; i < size; i++) {
            selectModeAdapter.removeItem(0);
        }
    }


    private void fillList(){
        ArrayList<BuiltInRecipeInfo> recipeFoods = builtRecipes.child;

        for(BuiltInRecipeInfo recipeInfo : recipeFoods){
            selectModeAdapter.addItem(recipeInfo.name);
        }

    }


    @Override
    public void itemClicked(View view, int position, String text) {
        Log.d(TAG, "itemclicked "+position);

        builtRecipes = (BuiltInRecipeFoodsInfo) builtRecipes.child.get(position);
        removeAllList();
        fillList();

//
//        switch(selectModeSteps){
//            case STEP_COOKING_METHOD:
//                if(text.equals("Sous Vide")){
//                    selectModeSteps = SelectModeSteps.STEP_MATERIAL;
//
//                    SetTitle(text);
//
//                    removeAllList();
//                    fillList(R.array.paragon_modes_sousvide);
//                    layoutButtons.setVisibility(View.GONE);
//                }
//                else{
//                    //do nothing
//                }
//
//                break;
//
//            case STEP_MATERIAL:
//                if(text.equals("Beef")){
//                    selectModeSteps = SelectModeSteps.STEP_HOW_TO_COOK;
//                    SetTitle(text);
//
//                    removeAllList();
//                    fillList(R.array.paragon_modes_beef);
//                    layoutButtons.setVisibility(View.GONE);
//                }
//                else{
//                    //do nothing
//                }
//
//                break;
//
//            case STEP_HOW_TO_COOK:
//                if(text.equals("Roast")){
//                    SetTitle("Settings");
//                    attached.nextStep(ParagonMainActivity.ParagonSteps.STEP_SOUSVIDE_SETTINGS);
//                }
//                else{
//                    //do nothing
//                }
//
//                break;
//        }

    }

    /**
     * Set title text on header.
     * @param text string to be title.
     */
    private void SetTitle(String text) {
        ((ParagonMainActivity)getActivity()).setTitle(text);
    }
}
