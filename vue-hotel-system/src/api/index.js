import request from '../utils/request';

export const fetchData = query => {
    return request({
                       url: './test.json',
                       method: 'get',
                       params: query
                   });
};


export const fetchData2 = query => {
    return request({
                       url: './test2.json',
                       method: 'get',
                       params: query
                   });
};