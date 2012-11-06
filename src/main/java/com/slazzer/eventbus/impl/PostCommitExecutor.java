package com.slazzer.eventbus.impl;

public interface PostCommitExecutor {

   void endTransaction();

   void performTransaction();

   void startNewTransaction();
}
