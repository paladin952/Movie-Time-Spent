package com.clpstudio.tvshowtimespent.datalayer.network.listener;

/**
 * Created by clapalucian on 11/9/16.
 */

public interface ApiListener<T> extends OnSuccess<T>, OnError<T> {
}
