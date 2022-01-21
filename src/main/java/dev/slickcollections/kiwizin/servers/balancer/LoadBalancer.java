package dev.slickcollections.kiwizin.servers.balancer;

import dev.slickcollections.kiwizin.servers.balancer.elements.LoadBalancerObject;

public interface LoadBalancer<T extends LoadBalancerObject> {
  T next();
}
