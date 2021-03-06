````sql
/*
{ 
  "name": "table", 
  "title": "Tables",
  "icon": "table_chart",
  "tags": "ui,schema"
}
*/
select table_schema      /* { "label": "Owner", "description": "Owner of the table", "visible": false } */
     , table_name        /* { "label": "Name", "description": "Name of the table" } */
     , table_type        /* { "label": "Type", "description": "Typical types", "visible": true, "values": ['TABLE', 'VIEW', 'SYSTEM TABLE', 'GLOBAL TEMPORARY', 'LOCAL TEMPORARY', 'ALIAS', 'SYNONYM'] } */
     , remarks           /* { "label": "Comment", "description": "Comments", "visible": true } */
     , last_modification /* { "label": "Last modification", "description": "Last modification" } */
  from information_schema.tables
````

````sql
/*
{ 
  "name": "column", 
  "title": "Columns",
  "icon": "view_column",
  "tags": "ui,schema"
}
*/
select table_schema               /* { "label": "owner", "description": "Owner of the table", "visible": true } */
     , table_name                 /* { "label": "table name", "description": "Name of the table", "visible": true } */
     , column_name                /* { "label": "column name", "description": "Name of the column", "visible": true } */
     , type_name                  /* { "label": "type name", "description": "Data source dependent type name, for a UDT the type name is fully qualified", "visible": true } */
     , character_maximum_length   /* { "label": "size", "description": "Column size", "visible": true } */
     , nullable                   /* { "label": "nullable", "description": "Is NULL allowed", "visible": true } */
     , column_default             /* { "label": "default value", "description": "Default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)" } */
  from information_schema.columns
````

````sql
/*
{ 
  "name": "constraint", 
  "title": "Constraints",
  "icon": "fa-key",
  "tags": "ui,schema"
}
*/
  select table_schema     /* { "label": "owner", "description": "Owner of the table", "visible": true } */
       , table_name       /* { "label": "table name", "description": "Name of the table", "visible": true } */
       , constraint_name  /* { "label": "pk", "description": "Primary key name", "visible": true } */
       , COLUMN_LIST
       , REMARKS
       , CHECK_EXPRESSION
    from information_schema.constraints
````

````sql
/*
{
  "name": "index",
  "title": "Indexes",
  "icon": "fa-indent",
  "tags": "ui,schema"
}
*/
select TABLE_SCHEMA     /* { "label": "owner", "description": "Owner of the table", "visible": true } */
     , TABLE_NAME       /* { "label": "table name", "description": "Name of the table", "visible": true } */
     , INDEX_NAME       /* { "label": "index name", "description": "Name of the index", "visible": true } */
     , ORDINAL_POSITION
     , COLUMN_NAME
     , CARDINALITY
     , PRIMARY_KEY
     , INDEX_TYPE_NAME
     , IS_GENERATED
     , INDEX_TYPE
     , ASC_OR_DESC
     , PAGES
     , FILTER_CONDITION
     , REMARKS
     , SQL
     , ID
     , SORT_TYPE
     , CONSTRAINT_NAME
     , INDEX_CLASS
     , AFFINITY
  from information_schema.indexes i
````
