package edu.mirea.onebeattrue.productlist.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import edu.mirea.onebeattrue.productlist.data.network.api.ApiFactory
import edu.mirea.onebeattrue.productlist.data.network.api.ApiService
import edu.mirea.onebeattrue.productlist.data.repository.ProductListRepositoryImpl
import edu.mirea.onebeattrue.productlist.data.repository.SearchRepositoryImpl
import edu.mirea.onebeattrue.productlist.domain.repository.ProductListRepository
import edu.mirea.onebeattrue.productlist.domain.repository.SearchRepository

@Module
interface DataModule {
    @[Binds ApplicationScope]
    fun bindProductListRepository(impl: ProductListRepositoryImpl): ProductListRepository

    @[Binds ApplicationScope]
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    companion object {
        @[Provides ApplicationScope]
        fun provideApiService(): ApiService = ApiFactory.apiService
    }
}