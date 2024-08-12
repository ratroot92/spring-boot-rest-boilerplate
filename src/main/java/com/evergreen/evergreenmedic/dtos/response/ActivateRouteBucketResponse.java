package com.evergreen.evergreenmedic.dtos.response;

import io.github.bucket4j.Bucket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateRouteBucketResponse {
    private Bucket clientBucket;
}
