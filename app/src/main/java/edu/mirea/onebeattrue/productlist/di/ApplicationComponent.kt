package edu.mirea.onebeattrue.productlist.di

import dagger.Component
import edu.mirea.onebeattrue.productlist.domain.repository.ProductListRepository

@ApplicationScope
@Component(modules = [DataModule::class, PresentationModule::class])
interface ApplicationComponent {

    fun getRepository(): ProductListRepository

    @Component.Factory
    interface Factory {
        fun create(): ApplicationComponent
    }
}