package com.steadytech.impian.helper

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.steadytech.impian.bottomsheet.ConfirmationBottomSheetFragment
import com.steadytech.impian.bottomsheet.DescriptionBottomSheetFragment
import com.steadytech.impian.bottomsheet.ErrorBottomSheetFragment
import com.steadytech.impian.bottomsheet.InfoBottomSheetFragment
import com.steadytech.impian.database.entity.EntitySaving
import com.steadytech.impian.model.realm.Saving

class BottomSheets {
    companion object {
        fun error(
            title: String,
            subtitle: String,
            useTitle: Boolean,
            useSubTitle: Boolean,
            buttonMessage: String,
            TAG: String,
            activity: AppCompatActivity
            ) {

            val bundle = Bundle()

            bundle.putString("title", title)
            bundle.putString("subtitle", subtitle)
            bundle.putBoolean("useTitle", useTitle)
            bundle.putBoolean("useSubtitle", useSubTitle)
            bundle.putString("buttonMessage", buttonMessage)

            val errorBottomSheetFragment = ErrorBottomSheetFragment()
            errorBottomSheetFragment.arguments = bundle
            errorBottomSheetFragment.show(activity.supportFragmentManager, TAG)
        }

        fun confirmation(
            title: String,
            subtitle: String,
            buttonPositiveMessage: String,
            TAG: String,
            activity: AppCompatActivity,
            listener : View.OnClickListener
            ) {

            val bundle = Bundle()

            bundle.putString("title", title)
            bundle.putString("subtitle", subtitle)
            bundle.putString("positiveMessage", buttonPositiveMessage)

            val confirmationBottomSheetFragment = ConfirmationBottomSheetFragment(listener)
            confirmationBottomSheetFragment.arguments = bundle
            confirmationBottomSheetFragment.show(activity.supportFragmentManager, TAG)
        }

        fun information(
            saving : EntitySaving,
            isCompleted : Boolean,
            activity : AppCompatActivity,
            listener : View.OnClickListener
        ){
            val infoBottomSheetFragment = InfoBottomSheetFragment(saving, listener, isCompleted)
            infoBottomSheetFragment.show(activity.supportFragmentManager, Constant.TAG.DetailGoalsActivity)
        }

        fun description(
            image : Int,
            title : String,
            description : String,
            activity: AppCompatActivity,
            tag : String
        ){
            val descriptionBottomSheet = DescriptionBottomSheetFragment(image, title, description)
            descriptionBottomSheet.show(activity.supportFragmentManager, tag)
        }
    }
}