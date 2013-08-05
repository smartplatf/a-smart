var ofForm = {
    LOOKUP : {
        "event" : "lookup",
        "html" : [{
            "name" : "group",
            "caption" : "Group",
            "type" : "text",
            "placeholder" : "Result Object Type",
            "validate" : {
                "required" : true,
                "messages" : {
                    "required" : "Required input",
                }
            }
        }, {
            "name" : "key",
            "caption" : "Key",
            "type" : "text",
            "placeholder" : "key",
            "validate" : {
                "required" : true,
                "messages" : {
                    "required" : "Required input",
                }
            }
        }, {
            "id" : "submit",
            "type" : "submit",
            "value" : "Find"
        }]
    },
    SEARCH : {
        "event" : "search",
        "html" : [{
            "name" : "group",
            "caption" : "Group",
            "type" : "text",
            "placeholder" : "Result Object Type",
            "validate" : {
                "required" : true,
                "messages" : {
                    "required" : "Required input",
                }
            }
        }, {
            "type" : "h5",
            "html" : "Query Map:"
        }, {
            "name" : "queryMap.name",
            "caption" : "Field",
            "type" : "text",
            "placeholder" : "Field Name",
            "validate" : {
                "required" : true,
                "messages" : {
                    "required" : "Required input",
                }
            }
        }, {
            "name" : "queryMap.value",
            "caption" : "Value",
            "type" : "text",
            "placeholder" : "with value",
            "validate" : {
                "required" : true,
                "messages" : {
                    "required" : "Required input",
                }
            }
        }, {
            "id" : "submit",
            "type" : "submit",
            "value" : "Search"
        }]
    },
    LISTALL : {
        "event" : "list",
        "html" : [{
            "name" : "group",
            "caption" : "Group",
            "type" : "text",
            "placeholder" : "Result Object Type",
            "validate" : {
                "required" : true,
                "messages" : {
                    "required" : "Required input",
                }
            }
        }, {
            "name" : "size",
            "id" : "size",
            "caption" : "Size",
            "type" : "text",
            "placeholder" : "Number of items",
            "validate" : {
                "required" : true,
                "messages" : {
                    "required" : "Required input",
                }
            }
        }, {
            "id" : "submit",
            "type" : "submit",
            "value" : "List"
        }]
    }
}