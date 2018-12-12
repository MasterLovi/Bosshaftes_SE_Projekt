var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":88,"id":35,"methods":[{"el":41,"sc":2,"sl":39},{"el":45,"sc":2,"sl":43},{"el":49,"sc":2,"sl":47},{"el":53,"sc":2,"sl":51},{"el":58,"sc":2,"sl":56},{"el":62,"sc":2,"sl":60},{"el":70,"sc":2,"sl":64},{"el":74,"sc":2,"sl":72},{"el":86,"sc":2,"sl":77}],"name":"Feedback","sl":19}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_1":{"methods":[{"sl":39},{"sl":43},{"sl":47},{"sl":51},{"sl":64},{"sl":77}],"name":"tFeedback","pass":true,"statements":[{"sl":40},{"sl":44},{"sl":48},{"sl":52},{"sl":65},{"sl":66},{"sl":68},{"sl":79},{"sl":85}]},"test_3":{"methods":[{"sl":39},{"sl":43},{"sl":47},{"sl":51},{"sl":60},{"sl":64},{"sl":72}],"name":"runLocation","pass":true,"statements":[{"sl":40},{"sl":44},{"sl":48},{"sl":52},{"sl":61},{"sl":65},{"sl":66},{"sl":73}]},"test_6":{"methods":[{"sl":39},{"sl":43},{"sl":47},{"sl":51},{"sl":60},{"sl":64},{"sl":72}],"name":"runRoute","pass":true,"statements":[{"sl":40},{"sl":44},{"sl":48},{"sl":52},{"sl":61},{"sl":65},{"sl":66},{"sl":73}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [1, 3, 6], [1, 3, 6], [], [], [1, 3, 6], [1, 3, 6], [], [], [1, 3, 6], [1, 3, 6], [], [], [1, 3, 6], [1, 3, 6], [], [], [], [], [], [], [], [3, 6], [3, 6], [], [], [1, 3, 6], [1, 3, 6], [1, 3, 6], [], [1], [], [], [], [3, 6], [3, 6], [], [], [], [1], [], [1], [], [], [], [], [], [1], [], [], []]
