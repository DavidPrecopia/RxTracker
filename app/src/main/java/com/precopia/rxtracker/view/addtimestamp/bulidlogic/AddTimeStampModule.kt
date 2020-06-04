package com.precopia.rxtracker.view.addtimestamp.bulidlogic

import android.content.Context
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.R
import com.precopia.rxtracker.common.buildlogic.ViewScope
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.addtimestamp.AddTimeStampLogic
import com.precopia.rxtracker.view.addtimestamp.IAddTimeStampContract
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Named

private const val THEME_CONTEXT = "theme_context"

@Module
class AddTimeStampModule {
    @ViewScope
    @Provides
    fun logic(
            view: Fragment,
            factory: ViewModelProvider.NewInstanceFactory
    ): IAddTimeStampContract.Logic {
        return ViewModelProvider(view, factory).get(AddTimeStampLogic::class.java)
    }

    @ViewScope
    @Provides
    fun factory(
            prescriptionRepo: IPrescriptionRepoContract,
            timeStampRepo: ITimeStampRepoContract,
            utilSchedulerProvider: IUtilSchedulerProviderContract,
            disposable: CompositeDisposable
    ): ViewModelProvider.NewInstanceFactory {
        return AddTimeStampLogicFactory(
                prescriptionRepo, timeStampRepo, utilSchedulerProvider, disposable
        )
    }

    @ViewScope
    @Provides
    fun adapter(@Named(THEME_CONTEXT) context: Context): ArrayAdapter<String> {
        return ArrayAdapter<String>(
                context,
                R.layout.spinner_list_style
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    @ViewScope
    @Provides
    @Named(THEME_CONTEXT)
    fun themeContext(activity: AppCompatActivity): Context {
        return activity.supportActionBar?.themedContext!!
    }
}