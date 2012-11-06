package com.slazzer.eventbus.utils;

import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.slazzer.eventbus.impl.PostCommitExecutor;

public class TransactionManagerWithPostCommitHandler extends HibernateTransactionManager {

   private static final long serialVersionUID = 4243592930773359989L;

   PostCommitExecutor postCommitExecutor;

   public PostCommitExecutor getPostCommitExecutor() {
      return postCommitExecutor;
   }

   public void setPostCommitExecutor(PostCommitExecutor postCommitExecutor) {
      this.postCommitExecutor = postCommitExecutor;
   }

   @Override
   protected void doBegin(Object transaction, TransactionDefinition definition) {
      postCommitExecutor.startNewTransaction();
      super.doBegin(transaction, definition);
   }

   @Override
   protected void doCleanupAfterCompletion(Object transaction) {
      postCommitExecutor.endTransaction();
      super.doCleanupAfterCompletion(transaction);
   }

   @Override
   protected void doCommit(DefaultTransactionStatus status) {
      super.doCommit(status);
      postCommitExecutor.performTransaction();
   }

}
