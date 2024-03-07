package edu.mirea.onebeattrue.productlist.di

import dagger.Component
import edu.mirea.onebeattrue.productlist.presentation.MainActivity

@ApplicationScope
@Component(modules = [DataModule::class, PresentationModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(): ApplicationComponent
    }
}