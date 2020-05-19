package com.precopia.rxtracker.common.buildlogic

import android.app.Application
import android.content.SharedPreferences
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.common.RxTrackerApplication
import com.precopia.rxtracker.util.IUtilNightModeContract
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.util.UtilSchedulerProvider
import com.precopia.rxtracker.view.manageprescriptionsview.ItemTouchHelperCallback
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
class ViewCommonModule {
    @ViewScope
    @Provides
    fun timeStampRepo(appComponent: AppComponent): ITimeStampRepoContract {
        return appComponent.timeStampRepo()
    }

    @ViewScope
    @Provides
    fun prescriptionRepo(appComponent: AppComponent): IPrescriptionRepoContract {
        return appComponent.prescriptionRepo()
    }

    @ViewScope
    @Provides
    fun utilNightMode(appComponent: AppComponent): IUtilNightModeContract {
        return appComponent.utilNightMode()
    }

    @ViewScope
    @Provides
    fun sharedPrefs(appComponent: AppComponent): SharedPreferences {
        return appComponent.sharedPrefs()
    }


    @ViewScope
    @Provides
    fun appComponent(application: Application): AppComponent {
        return (application as RxTrackerApplication).appComponent
    }


    @ViewScope
    @Provides
    fun disposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @ViewScope
    @Provides
    fun schedulerProvider(): IUtilSchedulerProviderContract {
        return UtilSchedulerProvider()
    }


    /**
     * RecyclerView
     */
    @Provides
    fun layoutManager(application: Application): LinearLayoutManager {
        return LinearLayoutManager(application.applicationContext)
    }

    @ViewScope
    @Provides
    fun itemDecoration(
            application: Application,
            layoutManager: LinearLayoutManager
    ): RecyclerView.ItemDecoration {
        return DividerItemDecoration(application.applicationContext, layoutManager.orientation)
    }

    @ViewScope
    @Provides
    fun itemTouchHelperCallback(movementCallback: ItemTouchHelperCallback.MovementCallback): ItemTouchHelper {
        return ItemTouchHelper(ItemTouchHelperCallback(movementCallback))
    }
}