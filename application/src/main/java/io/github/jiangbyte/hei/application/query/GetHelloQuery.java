package io.github.jiangbyte.hei.application.query;

import io.github.jiangbyte.hei.application.core.Query;

/**
 * 按 ID 查询 Hello 只读用例入参。
 */
public class GetHelloQuery implements Query {

    private final Long helloId;

    public GetHelloQuery(Long helloId) {
        this.helloId = helloId;
    }

    public Long getHelloId() {
        return helloId;
    }
}
