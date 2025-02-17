package com.example.service;

import com.example.entities.Pay;

import java.util.List;

/**
 * ClassName: PayService
 * Package: com.example.service
 * Description:
 *
 * @Author Harizon
 * @Create 2025/2/17 16:02
 * @Version 1.0
 */
public interface PayService
{
    public int add(Pay pay);
    public int delete(Integer id);
    public int update(Pay pay);

    public Pay   getById(Integer id);
    public List<Pay> getAll();
}
