package com.me.daggersample.ui.HeadLines.di

import com.me.daggersample.ui.HeadLines.data.HeadLinesRepository
import com.me.daggersample.ui.HeadLines.data.IHeadLinesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class HeadLinesModule {
    @Binds
    abstract fun provideIHeadLinesRepository(headLinesRepository: HeadLinesRepository): IHeadLinesRepository
}