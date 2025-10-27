package service;

import actor.ActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.Logger;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 定时脚本处理器
 */
@Singleton
public class FixedTimeExecutor {
    private final ActorSystem system;

    @Inject
    AppInit appInit;

    @Inject
    @Named("timerActor")
    ActorRef timerActorRef;


    @Inject
    @Named("tokenActor")
    ActorRef tokenActorRef;


    @Inject
    public FixedTimeExecutor(ActorSystem system) {
        this.system = system;
        Executor executor = Executors.newCachedThreadPool();
        CompletableFuture.runAsync(() -> schedule(), executor);
    }

    public void schedule() {
        appInit.saveToCache();

        system.scheduler().scheduleOnce(
                Duration.create(20, TimeUnit.MILLISECONDS),
                tokenActorRef,
                new ActorProtocol.STABLE_ACCESS_TOKEN(),
                system.dispatcher(),
                ActorRef.noSender()
        );

        system.scheduler().schedule(
                Duration.create(500, TimeUnit.MILLISECONDS),
                Duration.create(5, TimeUnit.SECONDS),
                tokenActorRef,
                new ActorProtocol.LISTEN_STABLE_ACCESS_TOKEN(),
                system.dispatcher(),
                ActorRef.noSender()
        );

        system.scheduler().schedule(
                Duration.create(600, TimeUnit.MILLISECONDS),
                Duration.create(10, TimeUnit.SECONDS),
                timerActorRef,
                new ActorProtocol.CACHE_POS_PRODUCTS(),
                system.dispatcher(),
                ActorRef.noSender()
        );

        system.scheduler().schedule(
                Duration.create(1, TimeUnit.SECONDS),
                Duration.create(3, TimeUnit.SECONDS),
                timerActorRef,
                new actor.ActorProtocol.NOTIFY_ORDER(),
                system.dispatcher(),
                ActorRef.noSender()
        );


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDay = now.plusDays(1);
        LocalTime min = LocalTime.MIN;
        LocalDateTime minNextDay = LocalDateTime.of(nextDay.toLocalDate(), min);
        long delay = Timestamp.valueOf(minNextDay).getTime() - Timestamp.valueOf(now).getTime();

    }


}
