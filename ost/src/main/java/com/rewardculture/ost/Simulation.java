package com.rewardculture.ost;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Simulates the 1000 transactions required by OST.
 *
 */
public class Simulation {
    public static void main(String[] args) throws IOException, InterruptedException {
        TokenEconomy ost = new OstEconomy();
        // 10 users some duplicated
        String[] uuids = {
                "46c30717-cc8c-45b8-b10b-6e51a6ddc08e",
                "3c0ee826-0abc-46de-ab8c-d8f757947208",
                "925059be-6bb1-4c56-9e51-d438830dd277",
                "7d17e608-ee64-4fae-8507-c034bc8b30f5",
                "3c0ee826-0abc-46de-ab8c-d8f757947208",
                "60023e89-3f2d-45d6-867f-6626634fdff3",
                "f2e7ebd1-5c92-4b5a-b81e-055924989f36",
                "0672484e-3d4e-4e39-86ea-62b81b94c6a6",
                "d1d78a37-bf39-42b9-ad09-d614d2d7c98d",
                "f2e7ebd1-5c92-4b5a-b81e-055924989f36",
        };
        // slightly different distribution to make it more real
        int[] amounts = {
                43, 29, 73, 101, 75, 11, 30, 9, 81
        };
        for (int i = 0; i < uuids.length; i++) {
            for (int j = 0; j < amounts[i]; j++) {
                System.out.printf("\ni = %d; j = %d", i, j);
                System.out.println(ost.executeReviewTransaction(uuids[i]));
                if (Math.random() < 0.5) TimeUnit.SECONDS.sleep(1);
                System.out.println(ost.executeLikeTransaction(uuids[i]));
            }
        }
    }
}
