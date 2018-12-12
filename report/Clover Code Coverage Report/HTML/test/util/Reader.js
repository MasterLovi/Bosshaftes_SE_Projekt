var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":30,"id":2072,"methods":[{"el":29,"sc":2,"sl":22}],"name":"Reader","sl":13}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_3":{"methods":[{"sl":22}],"name":"runLocation","pass":true,"statements":[{"sl":27},{"sl":28}]},"test_4":{"methods":[{"sl":22}],"name":"run","pass":true,"statements":[{"sl":27},{"sl":28}]},"test_5":{"methods":[{"sl":22}],"name":"run","pass":true,"statements":[{"sl":27},{"sl":28}]},"test_6":{"methods":[{"sl":22}],"name":"runRoute","pass":true,"statements":[{"sl":27},{"sl":28}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [3, 6, 4, 5], [], [], [], [], [3, 6, 4, 5], [3, 6, 4, 5], [], []]
